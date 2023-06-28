package com.gientech.iot.database.interceptor;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.gientech.iot.database.enums.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import static java.util.Arrays.stream;

/**
 * @description: ReadWrite读写分离拦截器
 * @author: 王强
 * @dateTime: 2023-02-19 20:09:05
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
public class ReadWriteInterceptor implements Interceptor {

    /**
     * @param invocation sql执行数据
     * @description: 拦截select语句，并将未指定数据源的方法切换成slave从库数据源，如果使用@DS注解指定数据源则使用指定的数据源
     * @author: 王强
     * @date: 2023-02-19 20:09:35
     * @return: Object
     * @version: 1.0
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.debug("执行intercept方法：{}", invocation.toString());
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        // sql语句类型 select、delete、insert、update
        String sqlCommandType = ms.getSqlCommandType().toString();
        // 仅拦截 select 查询, 设置select走从库
        if (!sqlCommandType.equals(SqlCommandType.SELECT.toString())) {
            return invocation.proceed();
        }
        // 获取查询语句Mapper的Class
        Class<?> aClass = Class.forName(ms.getId().substring(0, ms.getId().lastIndexOf(".")));
        // 获取查询语句对应的方法名
        String methodName = ms.getId().substring(ms.getId().lastIndexOf(".") + 1);
        // 判断类上是否使用@DS注解
        boolean clazzDs = aClass.getAnnotation(com.baomidou.dynamic.datasource.annotation.DS.class) != null;
        // 判断方法上是否使用@DS注解
        boolean methodDs = stream(aClass.getMethods()).filter(item -> item.getName().equals(methodName))
                .anyMatch(item -> item.getAnnotation(com.baomidou.dynamic.datasource.annotation.DS.class) != null);
        // 仅对未指定数据源的方法进行读写分离数据源的切换，已经指定数据源的方法则使用指定的数据源
        if (clazzDs || methodDs) {
            return invocation.proceed();
        }
        DynamicDataSourceContextHolder.push(DataSourceType.SLAVE.toString().toLowerCase());
        Object result = invocation.proceed();
        DynamicDataSourceContextHolder.poll();
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }
}
