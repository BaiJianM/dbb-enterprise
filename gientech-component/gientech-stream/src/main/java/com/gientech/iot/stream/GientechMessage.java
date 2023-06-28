package com.gientech.iot.stream;

import com.gientech.iot.web.commons.http.GientechException;
import com.gientech.iot.web.commons.http.SystemErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 自定义消息
 * @author: 白剑民
 * @dateTime: 2023/3/10 14:54
 */
@Getter
@ToString
@EqualsAndHashCode
public class GientechMessage implements Serializable {

    private static final long serialVersionUID = -5874282846565419944L;

    /**
     * 请求头(用以实现不同消息中间件的特殊消息)
     */
    private final Map<String, Object> headers;

    /**
     * 消息体
     */
    private final String msg;

    /**
     * 消息业务标识
     */
    private final String tag;

    public GientechMessage(Map<String, Object> headers, String msg, String tag) {
        this.headers = headers;
        this.msg = msg;
        this.tag = tag;
    }

    public static GientechMessage.GientechMessageBuilder builder() {
        return new GientechMessage.GientechMessageBuilder();
    }

    @NoArgsConstructor
    public static class GientechMessageBuilder {
        /**
         * 请求头
         */
        private Map<String, Object> headers;
        /**
         * 消息体
         */
        private String msg;
        /**
         * 消息业务标识
         */
        private String tag;

        public GientechMessage.GientechMessageBuilder header(@NonNull String headerName, @NonNull Object headerValue) {
            if (this.headers == null) {
                this.headers = new HashMap<>(16);
            }
            this.headers.put(headerName, headerValue);
            return this;
        }

        public GientechMessage.GientechMessageBuilder headers(Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }

        public GientechMessage.GientechMessageBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public GientechMessage.GientechMessageBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public GientechMessage build() {
            if (this.msg == null || this.msg.length() == 0
                    || this.tag == null || this.tag.length() == 0)
                throw new RuntimeException(SystemErrorCode.MESSAGE_BUILD_ERROR.getDescribe());
            // 默认的业务标识请求头属性
            if (this.headers == null) {
                this.headers = new HashMap<>(16);
            }
            this.headers.put(GientechMQConstant.Headers.TAGS, this.tag);
            return new GientechMessage(this.headers, this.msg, this.tag);
        }
    }
}
