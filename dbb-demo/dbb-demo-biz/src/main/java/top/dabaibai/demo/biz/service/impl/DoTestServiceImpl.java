package top.dabaibai.demo.biz.service.impl;

import com.alibaba.fastjson2.JSON;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.database.annotation.PessimisticLockInterceptor;
import top.dabaibai.demo.api.pojo.dto.DoTestDTO;
import top.dabaibai.demo.api.pojo.dto.TestOperationLogDTO;
import top.dabaibai.demo.api.pojo.vo.DoTestVO;
import top.dabaibai.demo.biz.entity.DoTest;
import top.dabaibai.demo.biz.mapper.DoTestMapper;
import top.dabaibai.demo.biz.service.DoTestService;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.test.api.interfaces.ITestService;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/3/2 15:12
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DoTestServiceImpl implements DoTestService {

    private final DoTestMapper testMapper;

    private final RedisUtils redisUtils;

    private final ITestService iTestService;

    private final Converter converter;

    @Override
    public void testGlobalTransaction() {
        DoTest test = new DoTest();
        test.setName("白白白白白");
        testMapper.insert(test);

        int i = 1 / 0;
    }

    @Override
    @PessimisticLockInterceptor(forUpdate = true)
    public List<DoTest> testForUpdateLock() {
        return testMapper.selectList(null);
    }

    @Override
    public void testSaveMaster() {
        DoTest doTest = new DoTest();
        doTest.setName("测试主库写入");
        testMapper.insert(doTest);
    }

    @Override
//    @DS("slave")
    public List<DoTest> testReadSlave() {
        return testMapper.selectList(null);
    }

    @Override
    public void testRedisBroadcast(String channel) {
        redisUtils.convertAndSend(channel, "这是一则重要广播通知");
    }

    @Override
    public void testVersion() {

        // DoTest doTest = testMapper.selectById(1657360048124215298L);
        // doTest.setUpdateUserId(doTest.getUpdateUserId() - 1);
        // int i1 = testMapper.updateById(doTest);
        testMapper.testUpdate(new TestOperationLogDTO());
        // new Thread(() -> {
        //     for (int i = 0; i < 50; i++) {
        //         DoTest doTest = testMapper.selectById(1657360048124215298L);
        //         doTest.setUpdateUserId(doTest.getUpdateUserId() - 1);
        //         int i1 = testMapper.updateById(doTest);
        //         log.info("第一个循环执行结果: {}", i1);
        //     }
        // }).start();
        //
        // for (int i = 0; i < 50; i++) {
        //     DoTest doTest = testMapper.selectById(1657360048124215298L);
        //     doTest.setUpdateUserId(doTest.getUpdateUserId() - 1);
        //     int i1 = testMapper.updateById(doTest);
        //     log.info("第二个循环执行结果: {}", i1);
        // }
    }

    @Override
    public void seata() {
        long s1 = System.currentTimeMillis();
        DoTest dt = new DoTest();
        dt.setName("测试分布式事务seata");
        testMapper.insert(dt);
        long s2 = System.currentTimeMillis();
        log.info("新增数据耗时: {}", (s2 - s1));

        iTestService.testSeata();
        log.info("服务请求耗时: {}", (System.currentTimeMillis() - s2));

        int i = 1 / 0;
    }

    @Override
    public void testCopy() {

        List<DoTest> list = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            DoTest dt = new DoTest();
            dt.setName("测试属性复制");
            list.add(dt);
        }

        long s1 = System.currentTimeMillis();
        List<DoTestDTO> dto = converter.convert(list, DoTestDTO.class);
        List<DoTestVO> vo = converter.convert(list, DoTestVO.class);
        log.info("Mapstruct转换耗时: {}", (System.currentTimeMillis() - s1));
        log.info("Mapstruct属性赋值结果VO: {}", JSON.toJSONString(vo.get(599)));

        long s2 = System.currentTimeMillis();
        Collection<DoTestDTO> dto1 = BeanConvertUtils.convertCollection(list, DoTestDTO::new).get();
        Collection<DoTestVO> vo1 = BeanConvertUtils.convertCollection(list, DoTestVO::new).get();
        log.info("BeanUtils转换耗时: {}", (System.currentTimeMillis() - s2));
        log.info("BeanUtils属性赋值结果VO: {}", JSON.toJSONString(vo1.stream().findFirst()));
    }
}
