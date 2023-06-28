package top.dabaibai.demo.biz.service;

import top.dabaibai.demo.biz.entity.DoTest;

import java.util.List;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/3/2 15:11
 */
public interface DoTestService {

    void testGlobalTransaction();

    List<DoTest> testForUpdateLock();

    void testSaveMaster();

    List<DoTest> testReadSlave();

    void testRedisBroadcast(String channel);

    void testVersion();

    void seata();

    void testCopy();
}
