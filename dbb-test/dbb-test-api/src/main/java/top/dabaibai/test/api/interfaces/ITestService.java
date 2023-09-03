package top.dabaibai.test.api.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.dabaibai.web.commons.http.DbbResponse;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/6/2 14:09
 */
@FeignClient(contextId = "ITestService", name = "dbb-test", path = "/test")
public interface ITestService {

    @GetMapping("/logic/testSeata")
    DbbResponse.ResponseBody<Void> testSeata();

}
