package com.gientech.iot.database.datapermission;

import cn.hutool.core.convert.Convert;
import com.gientech.iot.core.pojo.vo.BaseUserInfoVO;
import com.gientech.iot.core.utils.UserInfoUtils;
import com.gientech.iot.database.annotation.DataScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 权限解析器
 * @author: 白剑民
 * @dateTime: 2023/4/6 10:35
 */
@Slf4j
public class DataScopeAnnotationClassResolver {

    /**
     * 缓存方法对应的数据权限拦截属性
     */
    private final Map<Object, DataScopeParam> dsCache = new ConcurrentHashMap<>();

    public DataScopeAnnotationClassResolver() {
    }

    /**
     * @param method       方法
     * @param targetObject 目标对象
     * @description: 从缓存获取数据权限注解数据
     * @author: 白剑民
     * @date: 2023-04-07 10:01:05
     * @return: com.gientech.iot.mybatisplus.datapermission.DataScopeParam
     * @version: 1.0
     */
    public DataScopeParam findKey(Method method, Object targetObject) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        Object cacheKey = new MethodClassKey(method, targetObject.getClass());
        DataScopeParam dsp = this.dsCache.get(cacheKey);
        if (dsp == null) {
            dsp = computeDatasource(method, targetObject);
            this.dsCache.put(cacheKey, dsp);
        }
        return dsp;
    }

    /**
     * @param method       方法
     * @param targetObject 目标对象
     * @description: 查找注解
     * 顺序如下：
     * * 1. 当前方法
     * * 2. 桥接方法
     * * 3. 当前类开始一直找到Object
     * @author: 白剑民
     * @date: 2023-04-07 10:00:08
     * @return: com.gientech.iot.mybatisplus.datapermission.DataScopeParam
     * @version: 1.0
     */
    private DataScopeParam computeDatasource(Method method, Object targetObject) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        //1. 从当前方法接口中获取
        DataScopeParam dsAttr = findDataSourceAttribute(method);
        if (dsAttr != null) {
            return dsAttr;
        }
        Class<?> targetClass = targetObject.getClass();
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);

        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        //2. 从桥接方法查找
        dsAttr = findDataSourceAttribute(specificMethod);
        if (dsAttr != null) {
            return dsAttr;
        }
        // 从当前方法声明的类查找
        dsAttr = findDataSourceAttribute(userClass);
        if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return dsAttr;
        }
        //since 3.4.1 从接口查找，只取第一个找到的
        for (Class<?> interfaceClazz : ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            dsAttr = findDataSourceAttribute(interfaceClazz);
            if (dsAttr != null) {
                return dsAttr;
            }
        }
        // 如果存在桥接方法
        if (specificMethod != method) {
            // 从桥接方法查找
            dsAttr = findDataSourceAttribute(method);
            if (dsAttr != null) {
                return dsAttr;
            }
            // 从桥接方法声明的类查找
            dsAttr = findDataSourceAttribute(method.getDeclaringClass());
            if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
                return dsAttr;
            }
        }
        return getDefaultDataSourceAttr(targetObject);
    }

    /**
     * @param targetObject 目标查找对象
     * @description: 默认从当前类获取数据权限注解属性
     * @author: 白剑民
     * @date: 2023-04-07 09:59:18
     * @return: com.gientech.iot.mybatisplus.datapermission.DataScopeParam
     * @version: 1.0
     */
    private DataScopeParam getDefaultDataSourceAttr(Object targetObject) {
        Class<?> targetClass = targetObject.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                DataScopeParam datasourceAttr = findDataSourceAttribute(currentClass);
                if (datasourceAttr != null) {
                    return datasourceAttr;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * @param ae 目标查找对象
     * @description: 在目标对象中获取标记了数据权限注解的注解属性
     * @author: 白剑民
     * @date: 2023-04-07 09:57:29
     * @return: com.gientech.iot.mybatisplus.datapermission.DataScopeParam
     * @version: 1.0
     */
    private DataScopeParam findDataSourceAttribute(AnnotatedElement ae) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, DataScope.class);
        DataScopeParam dsp = null;
        if (attributes != null) {
            // 获取用户登录信息
            BaseUserInfoVO loginUser = UserInfoUtils.getUserInfo();
            // 如果没获取到用户信息则不往下执行
            if (loginUser == null) {
                log.warn("进行数据权限拦截时获取用户信息异常");
                return null;
            }
            boolean isFilter = attributes.getBoolean("isFilter");
            // 配置了不过滤数据则不往下执行
            if (!isFilter) {
                return null;
            } else {
                // 否则向数据权限过滤字段属性中赋值
                Map<String, String> filterFields = new HashMap<>(16);
                filterFields.put("department_code", loginUser.getDepartmentId().toString());
                dsp = new DataScopeParam(attributes.getString("defaultField"),
                        Collections.singleton(loginUser.getEnterpriseId()),
                        filterFields,
                        true,
                        Convert.toList(String.class, attributes.get("ignoreTables")));
            }
        }
        return dsp;
    }
}
