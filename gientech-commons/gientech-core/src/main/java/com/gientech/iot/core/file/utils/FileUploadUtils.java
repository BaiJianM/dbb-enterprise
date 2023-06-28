package com.gientech.iot.core.file.utils;

import com.gientech.iot.core.Constants;
import com.gientech.iot.core.enums.FileCheckMd5StatusEnum;
import com.gientech.iot.core.file.config.FileUploadProperties;
import com.gientech.iot.core.file.strategy.RandomAccessUploadStrategy;
import com.gientech.iot.core.pojo.dto.FileUploadDTO;
import com.gientech.iot.core.pojo.vo.FileUploadVO;
import com.gientech.iot.core.utils.FileMD5Utils;
import com.gientech.iot.core.utils.FileUtils;
import com.gientech.iot.core.utils.SpringContextUtils;
import com.gientech.iot.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 文件上传工具
 * @author: 王强
 * @dateTime: 2023-04-19 10:47:56
 */
@Slf4j
@Component
public class FileUploadUtils {

    private final RedisUtils redisUtil;

    private final FilePathUtils filePathUtils;

    private final CompletionService<FileUploadVO> completionService;

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public FileUploadUtils(RedisUtils redisUtil, FilePathUtils filePathUtils, FileUploadProperties properties) {
        this.redisUtil = redisUtil;
        this.filePathUtils = filePathUtils;
        ExecutorService executorService = Executors.newFixedThreadPool(properties.getMaxThreadSize(), (r) -> {
            Thread thread = new Thread(r);
            thread.setName("uploadPool-" + atomicInteger.getAndIncrement());
            return thread;
        });
        completionService = new ExecutorCompletionService<>(executorService, new LinkedBlockingDeque<>(properties.getMaxQueueSize()));
    }

    /**
     * @param dto 入参
     * @description: 上传
     * @author: 王强
     * @date: 2023-04-20 11:43:23
     * @return: FileUploadVO
     * @version: 1.0
     */
    public FileUploadVO upload(FileUploadDTO dto) throws IOException {
        if (Objects.isNull(dto.getChunkFile())) {
            throw new RuntimeException("file can not be empty");
        }
        dto.setPath(FileUtils.withoutHeadAndTailDiagonal(dto.getPath()));
        String md5 = FileMD5Utils.getFileMD5(dto.getChunkFile());
        dto.setMd5(md5);
        String filePath = filePathUtils.getPath(dto);
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        String path = filePath + File.separator + dto.getFileName();
        FileOutputStream out = new FileOutputStream(path);
        out.write(dto.getChunkFile().getBytes());
        out.flush();
        FileUtils.close(out);
        redisUtil.hPut(Constants.FileConstant.FILE_UPLOAD_STATUS, md5, "true");
        return FileUploadVO.builder().path(path).mtime(System.currentTimeMillis()).uploadComplete(true).build();
    }

    /**
     * @param dto 入参
     * @description: 分片上传
     * @author: 王强
     * @date: 2023-04-20 11:43:26
     * @return: FileUploadVO
     * @version: 1.0
     */
    public FileUploadVO sliceUpload(FileUploadDTO dto) {
        try {
            completionService.submit(new FileUploadCallable(dto));
            return completionService.take().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param dto 入参
     * @description: 检查文件md5
     * @author: 王强
     * @date: 2023-04-20 11:43:34
     * @return: FileUploadVO
     * @version: 1.0
     */
    public FileUploadVO checkFileMd5(FileUploadDTO dto) throws IOException {
        Object uploadProgressObj = redisUtil.hGet(Constants.FileConstant.FILE_UPLOAD_STATUS, dto.getMd5()).orElse(null);
        if (uploadProgressObj == null) {
            return FileUploadVO.builder().code(FileCheckMd5StatusEnum.FILE_NO_UPLOAD.getCode()).build();
        }
        String processingStr = uploadProgressObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = String.valueOf(redisUtil.get(Constants.FileConstant.FILE_MD5_KEY + dto.getMd5()).orElse(""));
        return fillFileUploadDTO(dto, processing, value);
    }

    /**
     * @param param      参数
     * @param processing 处理
     * @param value      检测值
     * @description: 填充返回文件内容信息
     * @author: 王强
     * @date: 2023-04-19 16:40:45
     * @return: FileUploadVO
     * @version: 1.0
     */
    private FileUploadVO fillFileUploadDTO(FileUploadDTO param, boolean processing, String value) throws IOException {
        if (processing) {
            param.setPath(FileUtils.withoutHeadAndTailDiagonal(param.getPath()));
            String path = filePathUtils.getPath(param);
            return FileUploadVO.builder().code(FileCheckMd5StatusEnum.FILE_UPLOAD_SUCCESS.getCode()).path(path).build();
        } else {
            File confFile = new File(value);
            byte[] completeList = org.apache.commons.io.FileUtils.readFileToByteArray(confFile);
            List<Integer> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i);
                }
            }
            return FileUploadVO.builder().code(FileCheckMd5StatusEnum.FILE_UPLOAD_SOME.getCode()).missChunks(missChunkList).build();
        }
    }

    /**
     * @description: 文件上传
     * @author: 王强
     * @dateTime: 2023-04-20 11:43:55
     */
    public static class FileUploadCallable implements Callable<FileUploadVO> {

        private final FileUploadDTO param;

        public FileUploadCallable(FileUploadDTO param) {
            this.param = param;
        }

        @Override
        public FileUploadVO call() {
            return SpringContextUtils.getBean(RandomAccessUploadStrategy.class).sliceUpload(param);
        }
    }

}
