package top.dabaibai.web.configuration.interceptor;

import com.alibaba.fastjson.JSON;
import top.dabaibai.core.Constants;
import top.dabaibai.core.pojo.vo.BaseUserInfoVO;
import top.dabaibai.core.utils.IpUtils;
import top.dabaibai.core.utils.UserInfoUtils;
import top.dabaibai.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Optional;

/**
 * @description: 自定义请求处理拦截器
 * @author: 白剑民
 * @dateTime: 2022-11-30 22:38:18
 */
@Slf4j
@SuppressWarnings("all")
public class CustomHandlerInterceptor implements HandlerInterceptor {

    private final RedisUtils redisUtils;

    public CustomHandlerInterceptor(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 获取request请求IP地址
            String ipAddress = IpUtils.getIpAddress(request);
            // 获取请求头中的用户信息（网关中添加）
            BaseUserInfoVO userInfo = JSON.parseObject(
                    URLDecoder.decode(request.getHeader("USER_INFO"), "UTF-8"), BaseUserInfoVO.class);
            // 用户信息中设置IP
            userInfo.setIpAddress(ipAddress);
            String key = Constants.LoginUser.LOGIN_USER_PREFIX + userInfo.getUserId();
            Optional<Object> redisUser = redisUtils.get(key);
            if (redisUser.isPresent()) {
                userInfo = JSON.parseObject(redisUser.get().toString(), BaseUserInfoVO.class);
            }
            // UserInfoUtils中设置当前用户登录信息
            UserInfoUtils.setUserInfo(userInfo);
        } catch (Exception e) {
            // 请求头中无用户信息不报错，所有接口通过网关，网关放行未校验token的则请求头中无用户信息
           // throw new DbbException(ResponseCode.GATEWAY_UN_PASS);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // UserInfoUtils中设置清除用户登录信息
        UserInfoUtils.clearUserInfo();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
