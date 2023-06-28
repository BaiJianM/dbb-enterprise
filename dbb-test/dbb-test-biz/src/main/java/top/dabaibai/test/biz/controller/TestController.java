package top.dabaibai.test.biz.controller;

import top.dabaibai.test.biz.service.TestService;
import top.dabaibai.web.commons.http.DbbResponse;
import top.dabaibai.web.configuration.annotations.DbbController;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/6/2 14:09
 */
@Slf4j
@DbbController("/logic")
@Tag(name = "测试控制层")
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TestController {

    private final TestService testService;

    @GetMapping("/doTest")
    public DbbResponse<Void> doTest() {
        log.info("测试日志");
        testService.doTest();
        return DbbResponse.success();
    }

    @GlobalTransactional
    @GetMapping("/testSeata")
    public DbbResponse<Void> testSeata() {
        long s = System.currentTimeMillis();
        testService.testSeata();
        return DbbResponse.success();
    }

}
