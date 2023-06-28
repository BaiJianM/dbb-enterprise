package com.gientech.iot.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import sun.misc.Cleaner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @description: 文件工具
 * @author: 王强
 * @dateTime: 2023-04-19 13:49:04
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {

    public static final String DOT = ".";
    public static final String SLASH_ONE = "/";
    public static final String SLASH_TWO = "\\";

    /**
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @description: 移动文件
     * @author: 王强
     * @date: 2023-04-20 09:15:32
     * @return: void
     * @version: 1.0
     */
    public static void moveFiles(String oldPath, String newPath) throws IOException {
        String[] filePaths = new File(oldPath).list();
        if (filePaths != null && filePaths.length > 0) {
            if (!new File(newPath).exists()) {
                new File(newPath).mkdirs();
            }
            for (int i = 0; i < filePaths.length; i++) {
                if (new File(oldPath + File.separator + filePaths[i]).isDirectory()) {
                    moveFiles(oldPath + File.separator + filePaths[i],
                            newPath + File.separator + filePaths[i]);
                } else if (new File(oldPath + File.separator + filePaths[i]).isFile()) {
                    copyFile(oldPath + File.separator + filePaths[i],
                            newPath + File.separator + filePaths[i]);
                    new File(oldPath + File.separator + filePaths[i])
                            .renameTo(new File(newPath + File.separator + filePaths[i]));
                }
            }
        }
    }

    /**
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @description: 复制文件
     * @author: 王强
     * @date: 2023-04-20 09:15:36
     * @return: void
     * @version: 1.0
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            File oldFile = new File(oldPath);
            File file = new File(newPath);
            FileInputStream in = new FileInputStream(oldFile);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[2097152];
            while ((in.read(buffer)) != -1) {
                out.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException("复制文件错误", e);
        }
    }

    /**
     * @param fileName 文件名称
     * @description: 获取含扩展名的文件名（不包含path路径）
     * @author: 王强
     * @date: 2023-04-20 09:15:40
     * @return: String
     * @version: 1.0
     */
    public static String getFileName(String fileName) {
        String name = "";
        if (StringUtils.lastIndexOf(fileName, SLASH_ONE) >= StringUtils.lastIndexOf(fileName, SLASH_TWO)) {
            name = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, SLASH_ONE) + 1, fileName.length());
        } else {
            name = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, SLASH_TWO) + 1, fileName.length());
        }
        return StringUtils.trimToEmpty(name);
    }

    /**
     * @param fileName 文件名称
     * @description: 获取没有扩展名的文件名
     * @author: 王强
     * @date: 2023-04-20 09:15:51
     * @return: String
     * @version: 1.0
     */
    public static String getWithoutExtension(String fileName) {
        String ext = StringUtils.substring(fileName, 0,
                StringUtils.lastIndexOf(fileName, DOT) == -1 ? fileName.length() : StringUtils.lastIndexOf(fileName, DOT));
        return StringUtils.trimToEmpty(ext);
    }

    /**
     * @param fileName 文件名称
     * @description: 获取扩展名
     * @author: 王强
     * @date: 2023-04-20 09:15:54
     * @return: String
     * @version: 1.0
     */
    public static String getExtension(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT)) {
            return StringUtils.EMPTY;
        }
        String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, DOT) + 1);
        return StringUtils.trimToEmpty(ext);
    }

    /**
     * @param fileName 文件名称
     * @param ext      ext
     * @description: 判断是否同为扩展名
     * @author: 王强
     * @date: 2023-04-20 09:15:56
     * @return: boolean
     * @version: 1.0
     */
    public static boolean isExtension(String fileName, String ext) {
        return StringUtils.equalsIgnoreCase(getExtension(fileName), ext);
    }

    /**
     * @param fileName 文件名称
     * @description: 判断是否存在扩展名
     * @author: 王强
     * @date: 2023-04-20 09:16:05
     * @return: boolean
     * @version: 1.0
     */
    public static boolean hasExtension(String fileName) {
        return !isExtension(fileName, StringUtils.EMPTY);
    }

    /**
     * @param ext ext
     * @description: 得到正确的扩展名
     * @author: 王强
     * @date: 2023-04-20 09:16:07
     * @return: String
     * @version: 1.0
     */
    public static String trimExtension(String ext) {
        return getExtension(DOT + ext);
    }

    /**
     * @param fileName 文件名称
     * @param ext      ext
     * @description: 向path中填充扩展名(如果没有或不同的话)
     * @author: 王强
     * @date: 2023-04-20 09:16:10
     * @return: String
     * @version: 1.0
     */
    public static String fillExtension(String fileName, String ext) {
        fileName = replacePath(fileName + DOT);
        ext = trimExtension(ext);
        if (!hasExtension(fileName)) {
            return fileName + getExtension(ext);
        }
        if (!isExtension(fileName, ext)) {
            return getWithoutExtension(fileName) + getExtension(ext);
        }
        return fileName;
    }

    /**
     * @param fileName 文件名称
     * @description: 判断是否是文件PATH
     * @author: 王强
     * @date: 2023-04-20 09:16:13
     * @return: boolean
     * @version: 1.0
     */
    public static boolean isFile(String fileName) {
        return hasExtension(fileName);
    }

    /**
     * @param fileName 文件名称
     * @description: 判断是否是文件夹PATH
     * @author: 王强
     * @date: 2023-04-20 09:16:16
     * @return: boolean
     * @version: 1.0
     */
    public static boolean isFolder(String fileName) {
        return !hasExtension(fileName);
    }

    public static String replacePath(String path) {
        return StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE,
                SLASH_TWO);
    }

    /**
     * @param path 路径
     * @description: 链接PATH前处理
     * @author: 王强
     * @date: 2023-04-20 09:16:19
     * @return: String
     * @version: 1.0
     */
    public static String trimLeftPath(String path) {
        if (isFile(path)) {
            return path;
        }
        path = replacePath(path);
        String top = StringUtils.left(path, 1);
        if (StringUtils.equalsIgnoreCase(SLASH_TWO, top)) {
            return StringUtils.substring(path, 1);
        }
        return path;
    }

    /**
     * @param path 路径
     * @description: 链接PATH后处理
     * @author: 王强
     * @date: 2023-04-20 09:16:28
     * @return: String
     * @version: 1.0
     */
    public static String trimRightPath(String path) {
        if (isFile(path)) {
            return path;
        }
        path = replacePath(path);
        String bottom = StringUtils.right(path, 1);
        if (StringUtils.equalsIgnoreCase(SLASH_TWO, bottom)) {
            return StringUtils.substring(path, 0, path.length() - 2);
        }
        return path + SLASH_TWO;
    }

    /**
     * @param path 路径
     * @description: 链接PATH前后处理，得到准确的链接PATH
     * @author: 王强
     * @date: 2023-04-20 09:16:37
     * @return: String
     * @version: 1.0
     */
    public static String trimPath(String path) {
        path = StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE, SLASH_TWO);
        path = trimLeftPath(path);
        path = trimRightPath(path);
        return path;
    }

    /**
     * @param paths 路径
     * @description: 通过数组完整链接PATH
     * @author: 王强
     * @date: 2023-04-20 09:16:39
     * @return: String
     * @version: 1.0
     */
    public static String buildFullPath(String... paths) {
        StringBuffer sb = new StringBuffer();
        for (String path : paths) {
            sb.append(trimPath(path));
        }
        return sb.toString();
    }

    /**
     * @param path 路径
     * @description: 去除首尾斜杠 path
     * @author: 王强
     * @date: 2023-04-20 09:16:46
     * @return: String
     * @version: 1.0
     */
    public static String withoutHeadAndTailDiagonal(String path) {
        int start = 0;
        int end = 0;
        boolean existHeadDiagonal = path.startsWith(File.separator);
        boolean existTailDiagonal = path.endsWith(File.separator);
        if (existHeadDiagonal && existTailDiagonal) {
            start = StringUtils.indexOf(path, File.separator, 0) + 1;
            end = StringUtils.lastIndexOf(path, File.separator);
            return StringUtils.substring(path, start, end);
        } else if (existHeadDiagonal && !existTailDiagonal) {
            start = StringUtils.indexOf(path, File.separator, 0) + 1;
            return StringUtils.substring(path, start);
        } else if (!existHeadDiagonal && existTailDiagonal) {
            end = StringUtils.lastIndexOf(path, File.separator);
            return StringUtils.substring(path, 0, end);
        }
        return path;
    }

    /**
     * @param filepath filepath
     * @description: del文件
     * @author: 王强
     * @date: 2023-04-20 09:16:50
     * @return: void
     * @version: 1.0
     */
    @SuppressWarnings("all")
    public static void delFile(String filepath) {
        // 定义文件路径
        File f = new File(filepath);
        //判断是文件还是目录
        if (f.exists() && f.isDirectory()) {
            if (f.listFiles().length != 0) {
                // 若有则把文件放进数组，并判断是否有下级目录
                File[] deleteFiles = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (deleteFiles[j].isDirectory()) {
                        // 递归调用del方法并取得子目录路径
                        delFile(deleteFiles[j].getAbsolutePath());
                    }
                    // 删除文件
                    deleteFiles[j].delete();
                }
            }
            f.delete();
        }
    }

    /**
     * @param filePaths 文件路径
     * @description: del文件列表
     * @author: 王强
     * @date: 2023-04-20 09:16:57
     * @return: void
     * @version: 1.0
     */
    public static void delFileList(List<String> filePaths) {
        for (String filePath : filePaths) {
            delFile(filePath);
        }
    }

    /**
     * @param filePath 文件路径
     * @description: 分离路径
     * @author: 王强
     * @date: 2023-04-20 09:17:00
     * @return: List<String>
     * @version: 1.0
     */
    public static List<String> splitPath(String filePath) {
        List<String> pathList = new ArrayList<>();
        if (filePath.contains(File.separator)) {
            String[] arrPath = StringUtils.split(filePath, File.separator);
            StringBuilder sbPath = new StringBuilder();
            for (int i = 0; i < arrPath.length - 1; i++) {
                sbPath.append(File.separator).append(arrPath[i]);
                pathList.add(sbPath.toString());
            }
        }
        return pathList;
    }

    /**
     * @param filePath 文件路径
     * @description: 获取父路径
     * @author: 王强
     * @date: 2023-04-20 09:17:03
     * @return: String
     * @version: 1.0
     */
    public static String getParentPath(String filePath) {
        if (StringUtils.lastIndexOf(filePath, SLASH_ONE) == 0) {
            return SLASH_ONE;
        } else {
            return StringUtils.substring(filePath, 0, StringUtils.lastIndexOf(filePath, SLASH_ONE));
        }
    }

    /**
     * @param directoryPath  需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     * @description: 获取路径下的所有文件/文件夹
     * @author: 王强
     * @date: 2023-04-20 09:17:06
     * @return: List<String>
     * @version: 1.0
     */
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }


    /**
     * @param name     名称
     * @param path     路径
     * @param request  请求
     * @param response 响应
     * @description: 下载文件
     * @author: 王强
     * @date: 2023-04-20 09:17:09
     * @return: void
     * @version: 1.0
     */
    public static void downloadFile(String name, String path, HttpServletRequest request,
                                    HttpServletResponse response) throws FileNotFoundException {
        File downloadFile = new File(path);
        String fileName = name;
        if (StringUtils.isBlank(fileName)) {
            fileName = downloadFile.getName();
        }
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        response.addHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        // 获取文件大小
        long downloadSize = downloadFile.length();
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            response.addHeader(HttpHeaders.CONTENT_LENGTH, downloadSize + "");
        } else {
            log.info("range:{}", response.getHeader("Range"));
            // 如果为持续下载
            response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            log.info("fronPos:{}", fromPos);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            response.addHeader(HttpHeaders.CONTENT_LENGTH, size + "");
            downloadSize = size;
        }

        try (RandomAccessFile in = new RandomAccessFile(downloadFile, "rw");
             OutputStream out = response.getOutputStream()) {
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
            byte[] buffer = new byte[bufLen];
            int num;
            // 当前写入客户端大小
            int count = 0;
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize - count);
                    if (bufLen == 0) {
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("download error:" + e.getMessage(), e);
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /**
     * @param str str
     * @description: 是否base64
     * @author: 王强
     * @date: 2023-04-20 09:17:36
     * @return: boolean
     * @version: 1.0
     */
    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * @param closeable closeable
     * @description: 关闭
     * @author: 王强
     * @date: 2023-04-20 09:17:38
     * @return: void
     * @version: 1.0
     */
    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                log.error("close fail:" + e.getMessage(), e);
            } finally {
            }
        }
    }

    /**
     * @param mappedByteBuffer 映射字节缓冲区
     * @description: 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，
     * 在并发情况下很容易发生 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     * @author: 王强
     * @date: 2023-04-20 09:17:41
     * @return: void
     * @version: 1.0
     */
    public static void freedMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }
            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(mappedByteBuffer, new Object[0]);
                    } catch (Exception e) {
                        log.error("clean MappedByteBuffer error!!!", e);
                    }
                    log.info("clean MappedByteBuffer completed!!!");
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param buffer 缓冲
     * @description: 清洁
     * @author: 王强
     * @date: 2023-04-20 09:18:17
     * @return: void
     * @version: 1.0
     */
    public static void clean(final Object buffer) {
        AccessController.doPrivileged(new PrivilegedAction() {
            @Override
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                } catch (Exception e) {
                    log.error("clean fail :" + e.getMessage(), e);
                }
                return null;
            }
        });
    }

    /**
     * @param in         在
     * @param byteBuffer 字节缓冲区
     * @description: 关闭
     * @author: 王强
     * @date: 2023-04-20 09:18:20
     * @return: void
     * @version: 1.0
     */
    public static void close(FileInputStream in, MappedByteBuffer byteBuffer) {
        if (null != in) {
            try {
                in.getChannel().close();
                in.close();
            } catch (IOException e) {
                log.error("close error:" + e.getMessage(), e);
            }
        }
        if (null != byteBuffer) {
            freedMappedByteBuffer(byteBuffer);
        }
    }
}
