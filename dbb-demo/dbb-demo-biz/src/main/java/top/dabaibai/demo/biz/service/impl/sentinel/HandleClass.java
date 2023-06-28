package top.dabaibai.demo.biz.service.impl.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import top.dabaibai.web.commons.http.DbbResponse;

public class HandleClass {
        public static DbbResponse<String> fallback(Throwable throwable) {
            return DbbResponse.success("熔断方法----------");
        }
        public static DbbResponse<String> block(BlockException blockException) {
            return DbbResponse.success("降级方法----------");
        }
    }