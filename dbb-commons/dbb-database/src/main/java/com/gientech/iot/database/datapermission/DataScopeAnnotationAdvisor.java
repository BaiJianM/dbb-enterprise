package com.gientech.iot.database.datapermission;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: 处理自定义切面
 * @author: 白剑民
 * @dateTime: 2023/4/6 10:43
 */
@Slf4j
public class DataScopeAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private static final long serialVersionUID = -2041719478088024634L;

    private final Advice advice;

    private final Pointcut pointcut;

    private final Class<? extends Annotation> annotation;

    public DataScopeAnnotationAdvisor(@NonNull MethodInterceptor advice,
                                      @NonNull Class<? extends Annotation> annotation) {
        this.advice = advice;
        this.annotation = annotation;
        this.pointcut = buildPointcut();
    }

    @NotNull
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @NotNull
    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(annotation, true);
        Pointcut mpc = new AnnotationMethodPoint(annotation);
        return new ComposablePointcut(cpc).union(mpc);
    }

    /**
     * In order to be compatible with the spring lower than 5.0
     */
    private static class AnnotationMethodPoint implements Pointcut {

        private final Class<? extends Annotation> annotationType;

        public AnnotationMethodPoint(Class<? extends Annotation> annotationType) {
            Assert.notNull(annotationType, "Annotation type must not be null");
            this.annotationType = annotationType;
        }

        @NotNull
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @NotNull
        @Override
        public MethodMatcher getMethodMatcher() {
            return new AnnotationMethodMatcher(annotationType);
        }

        private static class AnnotationMethodMatcher extends StaticMethodMatcher {
            private final Class<? extends Annotation> annotationType;

            public AnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
                this.annotationType = annotationType;
            }

            @Override
            public boolean matches(@NotNull Method method, @NotNull Class<?> targetClass) {
                if (matchesMethod(method)) {
                    return true;
                }
                // Proxy classes never have annotations on their redeclared methods.
                if (Proxy.isProxyClass(targetClass)) {
                    return false;
                }
                // The method may be on an interface, so let's check on the target class as well.
                Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
                return (specificMethod != method && matchesMethod(specificMethod));
            }

            private boolean matchesMethod(Method method) {
                return AnnotatedElementUtils.hasAnnotation(method, this.annotationType);
            }
        }
    }
}
