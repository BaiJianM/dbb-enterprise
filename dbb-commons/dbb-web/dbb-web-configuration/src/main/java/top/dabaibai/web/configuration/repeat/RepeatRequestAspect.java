package top.dabaibai.web.configuration.repeat;

import cn.hutool.core.util.CharUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.dabaibai.core.utils.UserInfoUtils;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.web.commons.http.DbbException;

import java.lang.reflect.Method;

/**
 * @description: 重复请求切面
 * @author: 白剑民
 * @dateTime: 2022-10-28 23:53:32
 */
@Order(-1)
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RepeatRequestAspect {

    private final RedisUtils redisUtils;

    /**
     * @param joinPoint 切点
     * @description: 接口幂等校验
     * @author: 白剑民
     * @date: 2023-04-26 10:48:48
     * @return: void
     * @version: 1.1
     */
    @Before("@annotation(top.dabaibai.web.configuration.repeat.RepeatRequest)")
    public void handle(JoinPoint joinPoint) throws NoSuchMethodException {
        RepeatRequest repeatRequest = this.getDeclaredAnnotation(joinPoint);
        String methodPath = joinPoint.getSignature().getDeclaringTypeName() + CharUtil.DOT + joinPoint.getSignature().getName();
        // 关于key的生成规则可以自己定义 本项目需求是对每个方法都加上限流功能，如果只是针对ip地址限流，那么key只需要只用ip就好
        String redisKey = UserInfoUtils.getUserInfo().getIpAddress() + methodPath;
        if (redisUtils.get(redisKey).isPresent()) {
            throw new DbbException(repeatRequest.message());
        } else {
            redisUtils.setEx(redisKey, redisKey, repeatRequest.expired(), repeatRequest.timeUnit());
        }
    }

    /**
     * @param joinPoint 连接点
     * @description: 获取方法中申明的注解
     * @author: 白剑民
     * @date: 2023-04-26 10:48:53
     * @return: RepeatRequest
     * @version: 1.0
     */
    public RepeatRequest getDeclaredAnnotation(JoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取指标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 依据类、方法、参数类型（重载）获取到办法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        return objMethod.getDeclaredAnnotation(RepeatRequest.class);
    }
}
