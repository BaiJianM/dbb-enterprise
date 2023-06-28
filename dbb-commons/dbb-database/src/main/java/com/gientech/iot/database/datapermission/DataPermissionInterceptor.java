package com.gientech.iot.database.datapermission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.gientech.iot.web.commons.http.GientechException;
import com.gientech.iot.web.commons.http.SystemErrorCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.aspectj.lang.annotation.Aspect;

import java.io.StringReader;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @description: 根据数据权限配置内容拦截并处理sql进行数据过滤
 * @author: 白剑民
 * @dateTime: 2023/4/6 11:06
 */
@Aspect
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataPermissionInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        DataScopeParam dataScopeParam = DataScopeParamContentHolder.get();
        // 没配置数据权限就不过滤
        if (dataScopeParam == null) {
            return invocation.proceed();
        }
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 先如果sql属性为FLUSH或UNKNOWN，则不执行过滤
        if (SqlCommandType.FLUSH.equals(mappedStatement.getSqlCommandType()) || SqlCommandType.UNKNOWN.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 执行的SQL语句
        String originalSql = boundSql.getSql();
        // SQL语句的参数
        Object parameterObject = boundSql.getParameterObject();
        // 拦截插入语句
        if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
            // 当为insert时将判断是否具备权限
            if (parameterObject != null) {
                Long enterpriseId = Convert.toLong(ReflectUtil.getFieldValue(parameterObject, StrUtil.toCamelCase(dataScopeParam.getDefaultField())));
                // 判断企业Id是否在权限范围内
                if (enterpriseId != null && !dataScopeParam.getEnterpriseIdList().contains(enterpriseId)) {
                    throw new GientechException(SystemErrorCode.DATA_ACCESS_DENIED);
                }
            }
            return invocation.proceed();
        }
        // 拦截更新语句，业务包含逻辑删除所以此处用的update
        if (SqlCommandType.UPDATE.equals(mappedStatement.getSqlCommandType())) {
            // 修改updateSql
            String updateSql = handleUpdateSql(originalSql, dataScopeParam.getEnterpriseIdList(), dataScopeParam.getDefaultField(), dataScopeParam.getIgnoreTables());
            log.debug("数据权限处理过后UPDATE的SQL: {}", updateSql);
            metaObject.setValue("delegate.boundSql.sql", updateSql);
            return invocation.proceed();
        }
        // 需要过滤的数据
        String finalSql = this.handleSql(originalSql, dataScopeParam.getEnterpriseIdList(),
                dataScopeParam.getDefaultField(), dataScopeParam.getIgnoreTables(), dataScopeParam.getFilterFields());
        log.debug("数据权限处理过后SELECT的SQL: {}", finalSql);
        // 装载改写后的sql
        metaObject.setValue("delegate.boundSql.sql", finalSql);
        return invocation.proceed();
    }

    /**
     * @param originalSql      原始sql
     * @param enterpriseIdList 需要过滤的企业列表
     * @param fieldName        表中待过滤查询的列名
     * @param ignores          忽略过滤的表名
     * @description: 修改select语句
     * @author: 白剑民
     * @date: 2023-04-07 09:44:20
     * @return: java.lang.String
     * @version: 1.0
     */
    private String handleSql(String originalSql, Set<Long> enterpriseIdList, String fieldName, List<String> ignores,
                             Map<String, String> filterFields) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(originalSql));
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody, enterpriseIdList, fieldName, ignores, filterFields);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s, enterpriseIdList, fieldName, ignores, filterFields));
        }
        return select.toString();
    }

    /**
     * @param originalSql      原始sql
     * @param enterpriseIdList 允许查询的企业列表
     * @param fieldName        表中待过滤查询的列名
     * @param ignores          忽略的表名
     * @description: 修改update语句
     * @author: 白剑民
     * @date: 2023-04-07 09:45:14
     * @return: java.lang.String
     * @version: 1.0
     */
    private String handleUpdateSql(String originalSql, Set<Long> enterpriseIdList, String fieldName, List<String> ignores) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Update update = (Update) parserManager.parse(new StringReader(originalSql));
        if (ignores.contains(update.getTable().getName())) {
            // 当前表名的处于不过滤列表则不进行二次封装处理
            return originalSql;
        }
        String dataPermissionSql;
        if (enterpriseIdList.size() == 1) {
            EqualsTo selfEqualsTo = new EqualsTo();
            selfEqualsTo.setLeftExpression(new Column(fieldName));
            selfEqualsTo.setRightExpression(new LongValue(enterpriseIdList.stream().findFirst().orElse(0L)));
            dataPermissionSql = selfEqualsTo.toString();
        } else {
            dataPermissionSql = fieldName + " in ( " + CollUtil.join(enterpriseIdList, StringPool.COMMA) + " )";
        }
        update.setWhere(new AndExpression(update.getWhere(), CCJSqlParserUtil.parseCondExpression(dataPermissionSql)));
        return update.toString();
    }

    /**
     * @param plainSelect      查询对象
     * @param enterpriseIdList 允许查询的企业列表
     * @param fieldName        表中待过滤查询的列名
     * @param ignores          忽略的表名
     * @description: 设置where条件（使用CCJSqlParser将原SQL进行解析并改写）
     * @author: 白剑民
     * @date: 2023-04-07 09:46:30
     * @version: 1.0
     */
    @SneakyThrows(Exception.class)
    protected void setWhere(PlainSelect plainSelect, Set<Long> enterpriseIdList, String fieldName,
                            List<String> ignores, Map<String, String> filterFields) {
        Table fromItem = (Table) plainSelect.getFromItem();
        // 有别名用别名，无别名用表名，防止字段冲突报错
        Alias fromItemAlias = fromItem.getAlias();
        if (ignores.contains(fromItem.getName())) {
            // 当前表名的处于不过滤列表则不进行二次封装处理
            return;
        }
        String mainTableName = fromItemAlias == null ? fromItem.getName() : fromItemAlias.getName();
        // 构建子查询 -- 数据权限过滤SQL
        StringBuilder dataPermissionSql;
        if (enterpriseIdList.size() == 1) {
            EqualsTo selfEqualsTo = new EqualsTo();
            selfEqualsTo.setLeftExpression(new Column(mainTableName + "." + fieldName));
            selfEqualsTo.setRightExpression(new LongValue(enterpriseIdList.stream().findFirst().orElse(0L)));
            dataPermissionSql = new StringBuilder(selfEqualsTo.toString());
        } else if (enterpriseIdList.size() < 1) {
            dataPermissionSql = new StringBuilder(mainTableName + "." + fieldName + " in ( " + StringPool.NULL + " )");
        } else {
            dataPermissionSql = new StringBuilder(mainTableName + "." + fieldName + " in ( " + CollUtil.join(enterpriseIdList, StringPool.COMMA) + " )");
        }

        if (!filterFields.isEmpty()) {
            for (Map.Entry<String, String> entry : filterFields.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                EqualsTo selfEqualsTo = new EqualsTo();
                selfEqualsTo.setLeftExpression(new Column(mainTableName + "." + key));
                selfEqualsTo.setRightExpression(new StringValue(val));
                dataPermissionSql.append(" and ");
                dataPermissionSql.append(selfEqualsTo);
            }
        }

        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(CCJSqlParserUtil.parseCondExpression(dataPermissionSql.toString()));
        } else {
            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), CCJSqlParserUtil.parseCondExpression(dataPermissionSql.toString())));
        }
    }

    @Override
    public Object plugin(Object target) {
        // 生成拦截对象的代理
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // mybatis配置的属性
        log.debug(properties.toString());
    }
}
