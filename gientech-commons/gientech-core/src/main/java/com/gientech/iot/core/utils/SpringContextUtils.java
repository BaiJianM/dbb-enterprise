package com.gientech.iot.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @description: spring上下文
 * @author: 白剑民
 * @dateTime: 2022-09-20 15:03:53
 */
@Slf4j
@Component
public class SpringContextUtils implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    /**
     * @param applicationContext 应用程序上下文
     * @description: 设置应用程序上下文
     * @author: 白剑民
     * @date: 2023-04-19 16:26:54
     * @return: void
     * @version: 1.0
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtils.applicationContext == null) {
          SpringContextUtils.applicationContext = applicationContext;
        }
        log.debug("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext=" + SpringContextUtils.applicationContext + "========");
    }

    /**
     * @description: 获取应用程序上下文
     * @author: 白剑民
     * @date: 2023-04-19 16:26:45
     * @return: ApplicationContext
     * @version: 1.0
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param name 名字
     * @description: 通过name获取Bean
     * @author: 白剑民
     * @date: 2023-04-19 16:26:43
     * @return: Object
     * @version: 1.0
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * @param clazz clazz
     * @description: 通过class获取Bean
     * @author: 白剑民
     * @date: 2023-04-19 16:26:41
     * @return: T
     * @version: 1.0
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * @param name  名字
     * @param clazz clazz
     * @description: 通过name, 以及Clazz返回指定的Bean
     * @author: 白剑民
     * @date: 2023-04-19 16:26:38
     * @return: T
     * @version: 1.0
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * @description: 获取请求
     * @author: 白剑民
     * @date: 2023-04-19 16:26:36
     * @return: HttpServletRequest
     * @version: 1.0
     */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * @description: 获取响应
     * @author: 白剑民
     * @date: 2023-04-19 16:26:35
     * @return: HttpServletResponse
     * @version: 1.0
     */
    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * @description: 清除SpringContextUtils中的ApplicationContext为Null.
     * @author: 白剑民
     * @date: 2023-04-19 16:26:12
     * @return: void
     * @version: 1.0
     */
    public static void clear() {
        applicationContext = null;
    }

    /**
     * @description: 实现DisposableBean接口, 在Context关闭时清理静态变量.
     * @author: 白剑民
     * @date: 2023-04-19 16:26:08
     * @return: void
     * @version: 1.0
     */
    @Override
    public void destroy() throws Exception {
        clear();
    }
}
