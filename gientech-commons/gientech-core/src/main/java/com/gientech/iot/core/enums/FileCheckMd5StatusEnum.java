package com.gientech.iot.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @description: 文件检查md5状态
 * @author: 白剑民
 * @dateTime: 2023-04-19 15:56:27
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileCheckMd5StatusEnum {

    /**
     * 文件已上传完成
     */
    FILE_UPLOAD_SUCCESS(200, "文件已上传完成！"),

    /**
     * 文件没有上传
     */
    FILE_NO_UPLOAD(404, "文件没有上传！"),

    /**
     * 文件已上传部分
     */
    FILE_UPLOAD_SOME(206, "文件已上传部分！");


    private final Integer code;

    private final String value;


    FileCheckMd5StatusEnum(Integer code, String name) {
        this.code = code;
        this.value = name;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
