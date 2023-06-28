package com.gientech.iot.core.file.strategy;

import com.gientech.iot.core.Constants;
import com.gientech.iot.core.file.utils.FilePathUtils;
import com.gientech.iot.core.pojo.dto.FileUploadDTO;
import com.gientech.iot.core.pojo.vo.FileUploadVO;
import com.gientech.iot.core.utils.FileMD5Utils;
import com.gientech.iot.core.utils.FileUtils;
import com.gientech.iot.core.utils.SpringContextUtils;
import com.gientech.iot.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 分片上传模板
 * @author: 白剑民
 * @dateTime: 2023-04-19 16:45:45
 */
@Slf4j
public abstract class SliceUploadTemplate implements SliceUploadStrategy {

    /**
     * @param param 文件上传参数
     * @description: 上传
     * @author: 白剑民
     * @date: 2023-04-19 16:45:35
     * @return: boolean
     * @version: 1.0
     */
    public abstract boolean upload(FileUploadDTO param);

    /**
     * @param param 文件上传参数
     * @description: 创建tmp文件
     * @author: 白剑民
     * @date: 2023-04-20 11:32:29
     * @return: File
     * @version: 1.0
     */
    protected File createTmpFile(FileUploadDTO param) {
        FilePathUtils filePathUtils = SpringContextUtils.getBean(FilePathUtils.class);
        param.setPath(FileUtils.withoutHeadAndTailDiagonal(param.getPath()));
        String uploadDirPath = filePathUtils.getPath(param);
        String tempFileName = param.getFileName() + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        return tmpFile;
    }

    @Override
    public FileUploadVO sliceUpload(FileUploadDTO param) {
        boolean isOk = this.upload(param);
        if (isOk) {
            File tmpFile = this.createTmpFile(param);
            return this.saveAndFileUploadDTO(param.getFileName(), tmpFile);
        }
        String md5 = FileMD5Utils.getFileMD5(param.getChunkFile());
        Map<Integer, String> map = new HashMap<>(16);
        map.put(param.getChunkIndex(), md5);
        return FileUploadVO.builder().chunkMd5Info(map).build();
    }

    /**
     * @param param         文件上传参数
     * @param uploadDirPath 上传dir路径
     * @description: 检查并修改文件上传进度
     * @author: 白剑民
     * @date: 2023-04-19 16:45:03
     * @return: boolean
     * @version: 1.0
     */
    public boolean checkAndSetUploadProgress(FileUploadDTO param, String uploadDirPath) {
        File confFile = new File(uploadDirPath, param.getFileName() + ".conf");
        byte isComplete = 0;
        RandomAccessFile accessConfFile = null;
        try {
            accessConfFile = new RandomAccessFile(confFile, "rw");
            // 把该分段标记为 true 表示完成
            log.info("set part " + param.getChunkIndex() + " complete");
            // 创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认0,已上传的就是Byte.MAX_VALUE 127
            accessConfFile.setLength(param.getChunkCount());
            accessConfFile.seek(param.getChunkIndex());
            accessConfFile.write(Byte.MAX_VALUE);

            // completeList 检查是否全部完成,如果数组里是否全部都是127(全部分片都成功上传)
            byte[] completeList = org.apache.commons.io.FileUtils.readFileToByteArray(confFile);
            isComplete = Byte.MAX_VALUE;
            for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
                // 与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
                isComplete = (byte) (isComplete & completeList[i]);
                log.info("check part " + i + " complete?:" + completeList[i]);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            FileUtils.close(accessConfFile);
        }
        return setUploadProgress2Redis(param, uploadDirPath, param.getFileName(), confFile, isComplete);
    }

    /**
     * @param param         参数
     * @param uploadDirPath 上传dir路径
     * @param fileName      文件名称
     * @param confFile      配置文件
     * @param isComplete    是否完整
     * @description: 把上传进度信息存进redis
     * @author: 白剑民
     * @date: 2023-04-19 16:45:06
     * @return: boolean
     * @version: 1.0
     */
    private boolean setUploadProgress2Redis(FileUploadDTO param, String uploadDirPath, String fileName, File confFile, byte isComplete) {
        RedisUtils redisUtil = SpringContextUtils.getBean(RedisUtils.class);
        if (isComplete == Byte.MAX_VALUE) {
            redisUtil.hPut(Constants.FileConstant.FILE_UPLOAD_STATUS, param.getMd5(), "true");
            redisUtil.delete(Constants.FileConstant.FILE_MD5_KEY + param.getMd5());
            confFile.delete();
            return true;
        } else {
            if (!redisUtil.hExists(Constants.FileConstant.FILE_UPLOAD_STATUS, param.getMd5()).orElse(Boolean.FALSE)) {
                redisUtil.hPut(Constants.FileConstant.FILE_UPLOAD_STATUS, param.getMd5(), "false");
                redisUtil.set(Constants.FileConstant.FILE_MD5_KEY + param.getMd5(), uploadDirPath + File.separator + fileName + ".conf");
            }
            return false;
        }
    }

    /**
     * @param fileName 文件名称
     * @param tmpFile  tmp文件
     * @description: 保存文件操作
     * @author: 白剑民
     * @date: 2023-04-19 16:45:09
     * @return: FileUploadVO
     * @version: 1.0
     */
    public FileUploadVO saveAndFileUploadDTO(String fileName, File tmpFile) {
        FileUploadVO fileUpload = null;
        try {
            fileUpload = renameFile(tmpFile, fileName);
            if (fileUpload.getUploadComplete()) {
                log.info("upload complete !!" + true + " name=" + fileName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {

        }
        return fileUpload;
    }

    /**
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @description: 文件重命名
     * @author: 白剑民
     * @date: 2023-04-19 16:45:11
     * @return: FileUploadVO
     * @version: 1.0
     */
    private FileUploadVO renameFile(File toBeRenamed, String toFileNewName) {
        // 检查要重命名的文件是否存在，是否是文件
        FileUploadVO fileUpload = new FileUploadVO();
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            log.info("File does not exist: {}", toBeRenamed.getName());
            fileUpload.setUploadComplete(false);
            return fileUpload;
        }
        String ext = FileUtils.getExtension(toFileNewName);
        String p = toBeRenamed.getParent();
        String filePath = p + File.separator + toFileNewName;
        File newFile = new File(filePath);
        // 修改文件名
        boolean uploadFlag = toBeRenamed.renameTo(newFile);
        fileUpload.setMtime(System.currentTimeMillis());
        fileUpload.setUploadComplete(uploadFlag);
        fileUpload.setPath(filePath);
        fileUpload.setSize(newFile.length());
        fileUpload.setFileExt(ext);
        fileUpload.setFileId(toFileNewName);
        return fileUpload;
    }

}
