package com.gientech.iot.core.file.strategy;

import com.gientech.iot.core.pojo.dto.FileUploadDTO;
import com.gientech.iot.core.pojo.vo.FileUploadVO;

/**
 * @description: 片上传策略
 * @author: 白剑民
 * @dateTime: 2023-04-19 16:44:41
 */
public interface SliceUploadStrategy {

    /**
     * @param param 参数
     * @description: 分片上传
     * @author: 白剑民
     * @date: 2023-04-19 16:44:43
     * @return: FileUploadVO
     * @version: 1.0
     */
    FileUploadVO sliceUpload(FileUploadDTO param);

}
