package com.gientech.iot.feign.intercepter;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @description: 自定义全局feign响应拦截器
 * @author: 白剑民
 * @dateTime: 2023/6/9 20:02
 */
@FunctionalInterface
public interface FeignResponseInterceptor {
    /**
     * @param request  原始请求对象
     * @param response 响应对象
     * @description: 拦截方法
     * @author: 白剑民
     * @date: 2023-06-09 21:08:15
     * @version: 1.0
     */
    void handle(Request request, Response response);
}
