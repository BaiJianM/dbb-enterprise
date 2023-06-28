package top.dabaibai.database.datapermission;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * @description: 拦截处理标注了数据权限注解的方法，并缓存自定义注解中的配置
 * @author: 白剑民
 * @dateTime: 2023/4/6 11:03
 */
@Slf4j
public class DataScopeAnnotationIntercept implements MethodInterceptor {

    private final DataScopeAnnotationClassResolver dataScopeAnnotationClassResolver;

    public DataScopeAnnotationIntercept() {
        dataScopeAnnotationClassResolver = new DataScopeAnnotationClassResolver();
    }

    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation methodInvocation) throws Throwable {
        // 根据方法找到对应缓存的数据权限注解属性并保存到线程变量中
        DataScopeParam param = dataScopeAnnotationClassResolver.findKey(methodInvocation.getMethod(), methodInvocation.getThis());
        DataScopeParamContentHolder.set(param);
        try {
            // 执行方法
            return methodInvocation.proceed();
        } finally {
            // 清空线程变量
            DataScopeParamContentHolder.clear();
        }
    }
}
