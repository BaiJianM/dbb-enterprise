package com.gientech.iot.demo.biz.service.impl.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.gientech.iot.web.commons.http.GientechResponse;

public class HandleClass {
        public static GientechResponse<String> fallback(Throwable throwable) {
            return GientechResponse.success("熔断方法----------");
        }
        public static GientechResponse<String> block(BlockException blockException) {
            return GientechResponse.success("降级方法----------");
        }
    }