package com.gientech.iot.web.commons.http;

/**
 * @description: 全局自定义异常类
 * @author: 白剑民
 * @dateTime: 2022/10/12 15:59
 */
public class GientechException extends RuntimeException implements ErrorResponse<Integer> {
    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误描述
     */
    private final String describe;

    public GientechException(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public GientechException(String describe) {
        this.code = SystemErrorCode.FAIL.getCode();
        this.describe = describe;
    }

    public GientechException(ErrorResponse<Integer> errorResponse) {
        this.code = errorResponse.getCode();
        this.describe = errorResponse.getDescribe();
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescribe() {
        return describe;
    }
}
