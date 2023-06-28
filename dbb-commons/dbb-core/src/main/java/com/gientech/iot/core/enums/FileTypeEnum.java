package com.gientech.iot.core.enums;

/**
 * @description: 文件类型枚举类
 * @author: 白剑民
 * @dateTime: 2022/7/13 14:27
 */
public enum FileTypeEnum {
    // 未知
    UNKNOWN,
    // 压缩文件
    ZIP, RAR, _7Z, TAR, GZ, TAR_GZ, BZ2, TAR_BZ2,
    // 图片文件
    BMP, JPG, JPEG, PNG, TIF, GIF, PCX, TGA, EXIF, FPX, CDR, PCD, DXF, UFO, EPS, AI, RAW, WMF, WEBP,
    // 矢量图文件
    SVG,
    // 影音文件
    AVI, MP4, MP3, AAR, OGG, WAV, WAVE,
    // 文本文件
    JSON, XML, TXT, CSV;

    /**
     * @description: 带后缀的文件类型常量类
     * @author: 白剑民
     * @dateTime: 2022/7/13 14:27
     */
    public interface FileSuffixConstant {
        /**
         * 文本文件类型
         */
        String XML = ".xml";
        String JSON = ".json";
        String TXT = ".txt";
        String CSV = ".csv";
        /**
         * 压缩文件类型
         */
        String ZIP = ".zip";
        String RAR = ".rar";
        String _7Z = ".7z";
        String TAR = ".tar";
        String GZ = ".gz";
        String TGZ = ".tgz";
        String BZ2 = ".bz2";
        String TAR_BZ2 = ".tar.bz2";
        /**
         * 图片文件类型
         */
        String BMP = ".bmp";
        String JPG = ".jpg";
        String JPEG = ".jpeg";
        String PNG = ".png";
        String TIF = ".tif";
        String GIF = ".gif";
        String PCX = ".pcx";
        String TGA = ".tga";
        String EXIF = ".exif";
        String FPX = ".fpx";
        String CDR = ".cdr";
        String DXF = ".dxf";
        String UFO = ".ufo";
        String EPS = ".eps";
        String AI = ".ai";
        String RAW = ".raw";
        String WMF = ".wmf";
        String WEBP = ".webp";
        /**
         * 矢量图文件类型
         */
        String SVG = ".svg";
        /**
         * 影音文件类型
         */
        String AVI = ".avi";
        String MP4 = ".mp4";
        String MP3 = ".mp3";
        String AAR = ".aar";
        String OGG = ".ogg";
        String WAV = ".wav";
        String WAVE = ".wave";
    }

    /**
     * @description: 图片类型后缀
     * @author: 刚呈靖
     * @dateTime: 2022/7/21 23:29
     */
    public static String[] picSuffix = {
            ".bmp", ".jpg", ".jpeg", ".png", ".tif",
            ".gif", ".svg", ".fpx", ".dxf", ".raw",
            ".wmf", ".webp"
    };

    /**
     * @param fileType 文件类型
     * @description: 根据文件类型获取后缀
     * @author: 张立檑
     * @date: 2022-09-14 17:45:42
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String getSuffix(FileTypeEnum fileType) {
        switch (fileType) {
            case TXT:
                return FileSuffixConstant.TXT;
            case XML:
                return FileSuffixConstant.XML;
            case CSV:
                return FileSuffixConstant.CSV;
            case ZIP:
                return FileSuffixConstant.ZIP;
            default:
                return FileSuffixConstant.JSON;
        }
    }
}
