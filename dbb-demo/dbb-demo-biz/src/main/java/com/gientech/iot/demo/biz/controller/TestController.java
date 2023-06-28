package com.gientech.iot.demo.biz.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.gientech.iot.core.file.utils.FileUploadUtils;
import com.gientech.iot.core.pojo.dto.FileUploadDTO;
import com.gientech.iot.core.pojo.vo.FileUploadVO;
import com.gientech.iot.demo.api.pojo.vo.DoTestVO;
import com.gientech.iot.demo.api.pojo.vo.TestValidateVO;
import com.gientech.iot.demo.biz.entity.DoTest;
import com.gientech.iot.demo.biz.service.DoTestService;
import com.gientech.iot.demo.biz.service.impl.queue.producer.TestProducer;
import com.gientech.iot.demo.biz.service.impl.sentinel.HandleClass;
import com.gientech.iot.demo.biz.stream.SeqStreamTest;
import com.gientech.iot.web.commons.http.GientechResponse;
import com.gientech.iot.web.configuration.annotations.GientechController;
import com.gientech.iot.websocket.operation.DefaultWebSocketSender;
import com.gientech.iot.websocket.vo.WebSocketMessageVO;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * @description: 测试类
 * @author: 白剑民
 * @dateTime: 2023/2/14 09:50
 */
@CrossOrigin
@Slf4j
@GientechController("/test")
@Tag(name = "测试控制层")
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TestController {

    private final DoTestService testService;

    private final TestProducer testProducer;

    private final DefaultWebSocketSender socketSender;

    private final FileUploadUtils fileUploadUtils;

    @Operation(summary = "验证手机号校验注解")
    @PostMapping("/authMobile")
    public String authMobile(@Valid @RequestBody TestValidateVO mobile) {
        return JSON.toJSONString(mobile);
    }

    @Operation(summary = "测试分布式事务注解")
    @PostMapping("/testGlobalTransaction")
    public void testGlobalTransaction() {
        testService.testGlobalTransaction();
    }

    @Operation(summary = "验证ForUpdate写锁注解")
    @PostMapping("/forUpdate")
    public List<DoTest> forUpdate() {
        return testService.testForUpdateLock();
    }

    @Operation(summary = "测试StreamBridgeRabbitmq")
    @GetMapping("/testStreamBridgeRabbit/{outputChannelName}")
    public void testStreamBridgeRabbit(@PathVariable String outputChannelName) {
        testProducer.testStreamBridgeRocketMQ(outputChannelName);
    }

    @Operation(summary = "测试StreamBridgeRocketmq事务消息")
    @GetMapping("/testStreamBridgeRocketTransaction")
    public void testStreamBridgeRocketTransaction() {
        testProducer.testStreamBridgeRocketTransaction();
    }

    @Operation(summary = "测试SaveMaster主库写入")
    @GetMapping("/testSaveMaster")
    public void testSaveMaster() {
        testService.testSaveMaster();
    }

    @Operation(summary = "测试ReadSlave从库读取")
    @GetMapping("/testReadSlave")
    public List<DoTest> testReadSlave() {
        return testService.testReadSlave();
    }

    @Operation(summary = "测试自定义流式API")
    @GetMapping("/testSeq")
    public long testSeq() {
        return SeqStreamTest.test();
    }

    @Operation(summary = "测试Redis广播通知")
    @GetMapping("/testRedisBroadcast/{channel}")
    public void testRedisBroadcast(@PathVariable String channel) {
        testService.testRedisBroadcast(channel);
    }

    @Operation(summary = "测试单节点WebSocket消息发送")
    @GetMapping("/testSingleWebSocket/{socketType}/{socketId}")
    public boolean testSingleWebSocket(@PathVariable String socketType, @PathVariable String socketId) {
        WebSocketMessageVO message = WebSocketMessageVO.builder()
                .socketType(socketType)
                .socketId(socketId)
                .content(String.valueOf(System.currentTimeMillis()))
                .build();
        return socketSender.sendMessage(message);
    }

    @Operation(summary = "测试分布式WebSocket消息发送")
    @GetMapping("/testWebSocket/{socketType}/{socketId}")
    public boolean testWebSocket(@PathVariable String socketType, @PathVariable String socketId) {
        WebSocketMessageVO message = WebSocketMessageVO.builder()
                .socketType(socketType)
                .socketId(socketId)
                .content(String.valueOf(System.currentTimeMillis()))
                .build();
        return socketSender.sendMessageByDistributed(message);
    }

    @Operation(summary = "测试基于WebSocket实现的文件上传实时进度获取")
    @PostMapping("/testWebSocketFileUpload")
    public void testWebSocketFileUpload(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Path path = Paths.get(System.getProperty("user.dir") + File.separator + Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream outputStream = new FileOutputStream(path.toFile());
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    @Operation(summary = "测试文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public GientechResponse<FileUploadVO> upload(FileUploadDTO dto, HttpServletRequest request) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        FileUploadVO upload;
        if (isMultipart) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("upload");
            if (dto.getChunkIndex() != null && dto.getChunkCount() > 1) {
                upload = fileUploadUtils.sliceUpload(dto);
            } else {
                upload = fileUploadUtils.upload(dto);
            }
            stopWatch.stop();
            log.info("{}", stopWatch.prettyPrint());
            return GientechResponse.success(upload);
        }
        throw new RuntimeException("上传失败");
    }

    @Operation(summary = "测试MD5文件校验")
    @RequestMapping(value = "/checkFileMd5", method = RequestMethod.POST)
    public GientechResponse<FileUploadVO> checkFileMd5(FileUploadDTO dto) throws IOException {
        return GientechResponse.success(fileUploadUtils.checkFileMd5(dto));
    }

    @Operation(summary = "测试mybatis-plus乐观锁重试机制")
    @GetMapping("/testVersion")
    public void testVersion() {
        testService.testVersion();
    }

    @Operation(summary = "测试分布式事务")
    @GlobalTransactional
    @GetMapping("/seata")
    public GientechResponse<DoTestVO> seata() {
        testService.seata();
        DoTestVO vo = new DoTestVO();
        vo.setName("1112222334455");
        return GientechResponse.success(vo);
    }

    @Operation(summary = "测试对象属性转换")
    @GetMapping("/testCopy")
    public void testCopy() {
        testService.testCopy();
    }

    @SentinelResource(value = "testSentinel",
            fallbackClass = HandleClass.class, fallback = "fallback",
            blockHandlerClass = HandleClass.class, blockHandler = "block")
    @GetMapping("/testSentinel")
    public GientechResponse<String> testSentinel() {
        return GientechResponse.success("成功");
    }
}
