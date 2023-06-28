package com.gientech.iot.web.configuration.handler;

import com.gientech.iot.web.commons.http.GientechException;
import com.gientech.iot.web.commons.http.GientechResponse;
import com.gientech.iot.web.commons.http.SystemErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 全局异常统一拦截类
 * @author: 白剑民
 * @dateTime: 2022/7/8 19:05
 */
@Slf4j
@RestControllerAdvice
public class CommonExtHandler {

    /**
     * @param e 未知异常类型
     * @description: 捕获全局异常, 处理所有不可知的异常
     * @author: 白剑民
     * @date: 2022-07-08 19:04:48
     * @return: java.lang.Object
     * @version: 1.0
     */
    @ExceptionHandler(value = Exception.class)
    public GientechResponse<?> handleException(Exception e) {
        e.printStackTrace();
        log.error("Exception异常: {}", e.getMessage());
        return GientechResponse.fail();
    }

    /**
     * @param e json异常类型
     * @description: 前后端参数不匹配校验（json解析异常）
     * @author: 白剑民
     * @date: 2022-07-09 12:32:48
     * @return: java.lang.Object
     * @version: 1.0
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public GientechResponse<?> validJson(HttpMessageNotReadableException e) {
        e.printStackTrace();
        log.info("Json异常: {}", e.getMessage());
        return GientechResponse.fail(SystemErrorCode.ILLEGAL_JSON, "", HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e http请求方式异常
     * @description: 接口请求方式不匹配异常
     * @author: 白剑民
     * @date: 2023-03-09 09:43:48
     * @return: com.gientech.iot.global.response.Response<?>
     * @version: 1.0
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public GientechResponse<?> validHttpMethod(HttpRequestMethodNotSupportedException e) {
        log.info("请求方法不匹配: {}", e.getMessage());
        String format = String.format("该接口支持的请求方法为 [%s]", Objects.requireNonNull(e.getSupportedMethods())[0]);
        return GientechResponse.fail(format, SystemErrorCode.NOT_SUPPORTED_METHOD, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 参数缺失异常
     * @description: 接口请求参数缺失
     * @author: 白剑民
     * @date: 2023-03-09 09:43:48
     * @return: com.gientech.iot.global.response.Response<?>
     * @version: 1.0
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public GientechResponse<?> validRequestParamMissing(MissingServletRequestParameterException e) {
        // 参数名
        String parameterName = e.getParameterName();
        // 参数类型
        String parameterType = e.getParameterType();
        log.info("请求参数缺失，参数名[{}]，参数类型[{}]", parameterName, parameterType);
        String msg = "参数名[%s]，参数类型[%s]";
        String format = String.format(msg, parameterName, parameterType);
        return GientechResponse.fail(format, SystemErrorCode.PARAMETER_MISSING, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 对象参数异常
     * @description: controller方法中，对象作为参数的校验
     * @author: 白剑民
     * @date: 2022-07-08 19:10:28
     * @return: com.gientech.iot.core.response.Response
     * @version: 1.0
     */
    @SuppressWarnings("all")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GientechResponse<?> validEntity(MethodArgumentNotValidException e) {
        Map<String, String> collect = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        String msg = String.join(",", collect.values());
        return GientechResponse.fail(msg, SystemErrorCode.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 表单及普通参数异常
     * @description: controller方法中，表单及普通参数的校验
     * @author: 白剑民
     * @date: 2022-07-08 19:13:35
     * @return: com.gientech.iot.core.response.Response
     * @version: 1.0
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public GientechResponse<?> validParam(ConstraintViolationException e) {
        Map<String, Object> collect = new HashMap<>(16);
        e.getConstraintViolations().forEach(c -> {
            PathImpl propertyPath = (PathImpl) c.getPropertyPath();
            NodeImpl leafNode = propertyPath.getLeafNode();
            String name = leafNode.getName();
            String value = c.getMessageTemplate();
            collect.put(name, value);
        });
        String msg = String.join(",", String.valueOf(collect.values()));
        return GientechResponse.fail(msg, SystemErrorCode.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 表单参数异常
     * @description: controller方法中：1、对象接收表单数据的校验；2、对象接收表单数据并做分组验参数据的校验
     * @author: 白剑民
     * @date: 2022-07-08 19:15:04
     * @return: com.gientech.iot.core.response.Response
     * @version: 1.0
     */
    @ExceptionHandler(BindException.class)
    public GientechResponse<?> exceptionHandler(BindException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, String> collect = new HashMap<>(16);
        allErrors.forEach(error -> {
            FieldError fieldError = (FieldError) error;
            collect.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        String msg = String.join(",", collect.values());
        return GientechResponse.fail(msg, SystemErrorCode.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 自定义异常
     * @description: 处理自定义异常类
     * @author: 白剑民
     * @date: 2022-07-08 19:14:23
     * @return: java.lang.Object
     * @version: 1.0
     */
    @ExceptionHandler(value = GientechException.class)
    public GientechResponse<String> handleGientechException(GientechException e) {
        log.error("GientechException异常: {}", e.getDescribe());
        return GientechResponse.warn(null, e);
    }

}
