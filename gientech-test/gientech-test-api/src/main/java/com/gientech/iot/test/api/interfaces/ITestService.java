package com.gientech.iot.test.api.interfaces;

import com.gientech.iot.web.commons.http.GientechResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/6/2 14:09
 */
@FeignClient(contextId = "ITestService", name = "gientech-test", path = "/test")
public interface ITestService {

    @GetMapping("/logic/testSeata")
    GientechResponse.ResponseBody<Void> testSeata();

}
