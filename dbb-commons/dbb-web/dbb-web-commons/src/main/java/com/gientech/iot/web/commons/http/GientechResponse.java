package com.gientech.iot.web.commons.http;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @description: 自定义接口请求响应
 * @author: 白剑民
 * @dateTime: 2023/4/23 14:45
 */
@Getter
public class GientechResponse<T> extends ResponseEntity<GientechResponse.ResponseBody<T>> {

    /**
     * 因bug产生的未知异常提示
     */
    private static final String ERROR_DESCRIPTION = "服务器异常";

    /**
     * 默认请求成功提示
     */
    private static final String SUCCESS_DESCRIPTION = "请求成功";

    /**
     * @param data   返回数据
     * @param msg    描述
     * @param code   业务状态码
     * @param status http状态码
     * @description: 自定义http响应体构造器
     * @author: 白剑民
     * @date: 2023-05-17 22:23:25
     * @version: 1.0
     */
    public GientechResponse(T data, String msg, String code, HttpStatus status) {
        super(new ResponseBody<>(data, msg, code), status);
    }

    public GientechResponse(ResponseBody<T> body, HttpStatus status) {
        super(body, status);
    }

    public static <T> GientechResponse<T> success() {
        return new GientechResponse<>(null, SUCCESS_DESCRIPTION, "200", HttpStatus.OK);
    }

    public static <T> GientechResponse<T> success(T data) {
        return new GientechResponse<>(data, SUCCESS_DESCRIPTION, "200", HttpStatus.OK);
    }

    public static <T> GientechResponse<T> success(T data, HttpStatus status) {
        return new GientechResponse<>(data, SUCCESS_DESCRIPTION, String.valueOf(status.value()), status);
    }

    public static <T> GientechResponse<T> success(T data, String msg, HttpStatus status) {
        return new GientechResponse<>(data, msg, String.valueOf(status.value()), status);
    }

    public static <T> GientechResponse<T> warn(T body, ErrorResponse<Integer> error) {
        return new GientechResponse<>(body, error.getDescribe(), error.getCode().toString(), HttpStatus.OK);
    }

    public static <T> GientechResponse<T> fail() {
        return new GientechResponse<>(null, ERROR_DESCRIPTION, "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> GientechResponse<T> fail(T data) {
        return new GientechResponse<>(data, ERROR_DESCRIPTION, "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> GientechResponse<T> fail(String msg) {
        return new GientechResponse<>(null, msg, "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> GientechResponse<T> fail(T data, String msg) {
        return new GientechResponse<>(data, msg, "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> GientechResponse<T> fail(T data, String msg, HttpStatus status) {
        return new GientechResponse<>(data, msg, String.valueOf(status.value()), status);
    }

    public static <T> GientechResponse<T> fail(T data, ErrorResponse<Integer> error) {
        return new GientechResponse<>(data, error.getDescribe(), "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> GientechResponse<T> fail(T data, ErrorResponse<Integer> error, HttpStatus status) {
        return new GientechResponse<>(data, error.getDescribe(), String.valueOf(status.value()), status);
    }

    /**
     * @description: 自定义接口请求响应体
     * @author: 白剑民
     * @dateTime: 2023/4/23 14:45
     */
    @Getter
    @Schema(description = "响应体")
    public static class ResponseBody<T> {

        @Schema(description = "响应数据内容")
        private final T data;

        @Schema(description = "描述信息")
        private final String msg;

        @Schema(description = "状态码（含自定义业务异常码与http状态码）")
        private final String code;

        public ResponseBody(T data, String msg, String code) {
            this.data = data;
            this.msg = msg;
            this.code = code;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(new ResponseBody<>(data, msg, code));
        }
    }
}
