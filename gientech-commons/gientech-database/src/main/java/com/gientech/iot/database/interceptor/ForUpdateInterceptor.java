package com.gientech.iot.database.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.gientech.iot.database.annotation.PessimisticLockInterceptor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StopWatch;

import java.sql.SQLException;
import java.util.List;

import static java.util.Arrays.stream;

/**
 * @description: 自定义悲观锁拦截器
 * @author: 王强
 * @dateTime: 2023-02-19 20:09:05
 */
@Slf4j
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ForUpdateInterceptor extends JsqlParserSupport implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        try {
            StopWatch stopWatch = null;
            if (log.isDebugEnabled()) {
                stopWatch = new StopWatch();
                stopWatch.start();
            }
            // 获取查询语句Mapper的Class
            Class<?> aClass = Class.forName(ms.getId().substring(0, ms.getId().lastIndexOf(".")));
            // 获取查询语句对应的方法名
            String methodName = ms.getId().substring(ms.getId().lastIndexOf(".") + 1);
            // 判断类上是否使用@PessimisticLockInterceptor注解，并设置forUpdate为true
            boolean clazzForUpdate = aClass.getAnnotation(PessimisticLockInterceptor.class) != null
                    && aClass.getAnnotation(PessimisticLockInterceptor.class).forUpdate();
            // 判断方法上是否使用@PessimisticLockInterceptor注解，并设置forUpdate为true
            boolean methodForUpdate = stream(aClass.getMethods()).filter(item -> item.getName().equals(methodName))
                    .anyMatch(item -> item.getAnnotation(PessimisticLockInterceptor.class) != null
                    && item.getAnnotation(PessimisticLockInterceptor.class).forUpdate());
            // 如果类或者方法上开启forUpdate，则在select语句后面添加 for update
            if (clazzForUpdate || methodForUpdate) {
                PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
                mpBs.sql(parserSingle(mpBs.sql(), ms.getId()));
            }
            if (log.isDebugEnabled()) {
                assert stopWatch != null;
                stopWatch.stop();
                log.debug("ForUpdate拦截耗时: {}", stopWatch.getTotalTimeMillis());
            }
        } catch (ClassNotFoundException e) {
            log.error("ForUpdate拦截异常: {}", e.getMessage());
        }
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            // 语句后面添加 for update
            ((PlainSelect) selectBody).setForUpdate(true);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            // 语句后面添加 for update
            selectBodyList.forEach(s -> ((PlainSelect) s).setForUpdate(true));
        }
    }
}

