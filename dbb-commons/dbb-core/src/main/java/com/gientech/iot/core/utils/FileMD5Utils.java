package com.gientech.iot.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: 计算文件MD5工具类
 * @author: 白剑民
 * @dateTime: 2023-04-19 16:48:26
 */
@Slf4j
public class FileMD5Utils {

    /**
     * @param file 文件
     * @description: 获取文件md5
     * @author: 白剑民
     * @date: 2023-04-19 16:48:29
     * @return: String
     * @version: 1.0
     */
    public static String getFileMD5(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        MappedByteBuffer byteBuffer = null;
        try {
            byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
            if (value.length() < 32) {
                value = "0" + value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(in, byteBuffer);
        }
        return value;
    }

    /**
     * @param file 文件
     * @description: 获取文件md5
     * @author: 白剑民
     * @date: 2023-04-19 16:48:31
     * @return: String
     * @version: 1.0
     */
    public static String getFileMD5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            return new BigInteger(1, digest).toString(16);
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("get file md5 error!!!", e);
        }
        return null;
    }
}
