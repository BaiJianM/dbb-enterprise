package top.dabaibai.log.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import top.dabaibai.log.core.pojo.LogDTO;
import top.dabaibai.log.core.service.ILogService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * @description: 操作日志默认存储至本地文件
 * @author: 白剑民
 * @dateTime: 2023/4/6 15:32
 */
@Slf4j
public class DefaultLogServiceImpl implements ILogService {

    /**
     * 操作日志存储文件名
     */
    private static final String STORE_FILENAME = "OperationLog.json";
    /**
     * 操作日志文件存储路径
     */
    private static final String STORE_PATH = System.getProperty("user.dir") + File.separator + STORE_FILENAME;

    @Override
    public void createLog(LogDTO logDTO) {
        File file = new File(STORE_PATH);
        try {
            if (!file.exists()) {
                // 不存在则生成
                file.createNewFile();
            }
            // 操作日志文件写入（增量）
            BufferedWriter bufferedWriter =
                    Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            bufferedWriter.write(JSON.toJSONString(logDTO, SerializerFeature.PrettyFormat));
            bufferedWriter.flush();
        } catch (IOException e) {
            log.error("操作日志存储出错，错误原因: {}", e.getMessage());
        }
    }
}
