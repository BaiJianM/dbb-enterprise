package top.dabaibai.core.file.strategy;

import top.dabaibai.core.pojo.dto.FileUploadDTO;
import top.dabaibai.core.pojo.vo.FileUploadVO;

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
