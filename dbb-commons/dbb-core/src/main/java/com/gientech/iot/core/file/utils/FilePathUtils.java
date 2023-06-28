package com.gientech.iot.core.file.utils;

import com.gientech.iot.core.file.config.FileUploadProperties;
import com.gientech.iot.core.pojo.dto.FileUploadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @description: 文件路径工具
 * @author: 白剑民
 * @dateTime: 2023-04-19 16:20:23
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilePathUtils {

    private final FileUploadProperties properties;

    /**
     * @description: 初始化时创建文件上传根路径文件夹
     * @author: 白剑民
     * @date: 2023-04-20 11:46:29
     * @return: void
     * @version: 1.0
     */
    public void createRootDir() {
        File file = Paths.get(getRootPath()).toFile();
        if (!file.mkdirs()) {
            file.mkdirs();
        }
    }

    /**
     * @description: 获取根路径
     * @author: 白剑民
     * @date: 2023-04-20 09:40:48
     * @return: String
     * @version: 1.0
     */
    private String getRootPath() {
        return Objects.nonNull(properties.getRootDir()) ? properties.getRootDir() : System.getProperty("user.dir");
    }

    /**
     * @param param 文件上传参数
     * @description: 根据文件上传参数获取文件保存路径
     * @author: 白剑民
     * @date: 2023-04-20 09:40:50
     * @return: String
     * @version: 1.0
     */
    public String getPath(FileUploadDTO param) {
        return getRootPath() + File.separator + param.getPath() + File.separator + param.getMd5();
    }

}
