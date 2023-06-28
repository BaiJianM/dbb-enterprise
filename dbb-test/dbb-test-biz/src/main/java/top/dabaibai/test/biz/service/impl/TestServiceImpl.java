package top.dabaibai.test.biz.service.impl;

import top.dabaibai.test.biz.entity.LogicTest;
import top.dabaibai.test.biz.mapper.LogicTestMapper;
import top.dabaibai.test.biz.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/6/2 13:54
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TestServiceImpl implements TestService {

    private final LogicTestMapper logicTestMapper;

    @Override
    public void doTest() {
        LogicTest test = new LogicTest();
        test.setParam_1("11122");
        test.setParam_2("33333");
        logicTestMapper.insert(test);
    }

    @Override
    public void testSeata() {
        long s1 = System.currentTimeMillis();
        LogicTest lt = new LogicTest();
        lt.setParam_2("测试seata");
        lt.setParam_1("测试seata");
        logicTestMapper.insert(lt);
        long s2 = System.currentTimeMillis();
        log.info("新增数据耗时: {}", (s2 - s1));
    }
}
