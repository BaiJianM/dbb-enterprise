package com.gientech.iot.demo.biz.service.impl.feign;

import com.gientech.iot.feign.intercepter.FeignResponseInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/6/15 16:01
 */
@Slf4j
@Component
public class CustomFeignResponseInterceptor implements FeignResponseInterceptor {
    @Override
    public void handle(Request request, Response response) {
        // 拦截逻辑
    }
}
