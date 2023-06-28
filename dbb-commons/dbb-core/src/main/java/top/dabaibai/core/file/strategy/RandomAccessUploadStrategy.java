package top.dabaibai.core.file.strategy;

import top.dabaibai.core.file.utils.FilePathUtils;
import top.dabaibai.core.pojo.dto.FileUploadDTO;
import top.dabaibai.core.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @description: 随机访问上传策略
 * @author: 白剑民
 * @dateTime: 2023-04-19 16:42:43
 */
@Slf4j
@Component
public class RandomAccessUploadStrategy extends SliceUploadTemplate {

    @Autowired
    private FilePathUtils filePathUtils;

    @Override
    public boolean upload(FileUploadDTO param) {
        RandomAccessFile accessTmpFile = null;
        try {
            String uploadDirPath = filePathUtils.getPath(param);
            File tmpFile = super.createTmpFile(param);
            accessTmpFile = new RandomAccessFile(tmpFile, "rw");
            // 这个必须与前端设定的值一致
            long chunkSize = param.getChunkSize();
            long offset = chunkSize * param.getChunkIndex();
            // 定位到该分片的偏移量
            accessTmpFile.seek(offset);
            // 写入该分片数据
            accessTmpFile.write(param.getChunkFile().getBytes());
            return super.checkAndSetUploadProgress(param, uploadDirPath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            FileUtils.close(accessTmpFile);
        }
        return false;
    }

}
