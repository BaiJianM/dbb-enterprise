package com.gientech.iot.log.core.function;

import com.gientech.iot.log.annotations.LogFunction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 注册自定义函数
 * @author: 白剑民
 * @dateTime: 2022-09-02 15:57:45
 */
@Data
@Slf4j
public class CustomFunctionRegistrar implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static Map<String, Method> functionMap = new HashMap<>();

    /**
     * @param applicationContext 应用程序上下文
     * @description: 扫描使用@LogRecordFunc注解申明的自定义函数
     * @author: 白剑民
     * @date: 2022-09-02 17:23:39
     * @return:
     * @version: 1.0
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(LogFunction.class);
        beanWithAnnotation.values()
            .forEach(
                component -> {
                    Method[] methods = component.getClass().getMethods();
                    LogFunction classFunc = component.getClass().getAnnotation(LogFunction.class);
                    String prefixName = classFunc.value();
                    if (StringUtils.hasText(prefixName)) {
                        prefixName += "_";
                    }
                    if (methods.length > 0) {
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(LogFunction.class) && isStaticMethod(method)) {
                                LogFunction func = method.getAnnotation(LogFunction.class);
                                String registerName =
                                        StringUtils.hasText(func.value()) ? func.value() : method.getName();
                                functionMap.put(prefixName + registerName, method);
                                log.info("LogRecord register custom function [{}] as name [{}]",
                                        method, prefixName + registerName);
                            }
                        }
                    }
                }
            );
    }

    /**
     * @param context 上下文
     * @description: 注册
     * @author: 白剑民
     * @date: 2022-09-02 17:23:32
     * @return:
     * @version: 1.0
     */
    public static void register(StandardEvaluationContext context) {
        functionMap.forEach(context::registerFunction);
    }

    /**
     * @param method 待判断的方法
     * @description: 判断是否为静态方法
     * @author: 白剑民
     * @date: 2022-09-02 18:18:51
     * @return: @return boolean
     * @version: 1.0
     */
    private static boolean isStaticMethod(Method method) {
        if (method == null) {
            return false;
        }
        int modifiers = method.getModifiers();
        return Modifier.isStatic(modifiers);
    }
}
