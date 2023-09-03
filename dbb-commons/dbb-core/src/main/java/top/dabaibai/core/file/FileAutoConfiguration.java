package top.dabaibai.core.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.dabaibai.core.file.config.FileUploadProperties;
import top.dabaibai.core.file.strategy.RandomAccessUploadStrategy;
import top.dabaibai.core.file.utils.FilePathUtils;
import top.dabaibai.core.file.utils.FileUploadUtils;

import javax.annotation.PostConstruct;

/**
 * @description: 文件自动配置
 * @author: 白剑民
 * @dateTime: 2023-04-20 10:53:49
 */
@Configuration
@EnableConfigurationProperties(FileUploadProperties.class)
@Import(value = {FileUploadUtils.class, FilePathUtils.class, RandomAccessUploadStrategy.class})
public class FileAutoConfiguration {

    @Autowired
    private FilePathUtils filePathUtils;

    @PostConstruct
    public void init() {
        // 初始化时创建文件上传根路径文件夹
        filePathUtils.createRootDir();
    }
}
