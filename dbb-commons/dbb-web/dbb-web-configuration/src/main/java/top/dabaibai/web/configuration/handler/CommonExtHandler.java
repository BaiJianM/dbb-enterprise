package top.dabaibai.web.configuration.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.dabaibai.web.commons.http.DbbException;
import top.dabaibai.web.commons.http.DbbResponse;
import top.dabaibai.web.commons.http.SystemErrorCode;

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
public class CommonExtHandler implements ResponseBodyAdvice<Object> {

    /**
     * @param e 未知异常类型
     * @description: 捕获全局异常, 处理所有不可知的异常
     * @author: 白剑民
     * @date: 2022-07-08 19:04:48
     * @return: java.lang.Object
     * @version: 1.0
     */
    @ExceptionHandler(value = Exception.class)
    public DbbResponse<?> handleException(Exception e) {
        e.printStackTrace();
        log.error("Exception异常: {}", e.getMessage());
        return DbbResponse.fail();
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
    public DbbResponse<?> validJson(HttpMessageNotReadableException e) {
        e.printStackTrace();
        log.info("Json异常: {}", e.getMessage());
        return DbbResponse.fail(SystemErrorCode.ILLEGAL_JSON, "", HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e http请求方式异常
     * @description: 接口请求方式不匹配异常
     * @author: 白剑民
     * @date: 2023-03-09 09:43:48
     * @return: top.dabaibai.global.response.Response<?>
     * @version: 1.0
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public DbbResponse<?> validHttpMethod(HttpRequestMethodNotSupportedException e) {
        log.info("请求方法不匹配: {}", e.getMessage());
        String format = String.format("该接口支持的请求方法为 [%s]", Objects.requireNonNull(e.getSupportedMethods())[0]);
        return DbbResponse.fail(format, SystemErrorCode.NOT_SUPPORTED_METHOD, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 参数缺失异常
     * @description: 接口请求参数缺失
     * @author: 白剑民
     * @date: 2023-03-09 09:43:48
     * @return: top.dabaibai.global.response.Response<?>
     * @version: 1.0
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public DbbResponse<?> validRequestParamMissing(MissingServletRequestParameterException e) {
        // 参数名
        String parameterName = e.getParameterName();
        // 参数类型
        String parameterType = e.getParameterType();
        log.info("请求参数缺失，参数名[{}]，参数类型[{}]", parameterName, parameterType);
        String msg = "参数名[%s]，参数类型[%s]";
        String format = String.format(msg, parameterName, parameterType);
        return DbbResponse.fail(format, SystemErrorCode.PARAMETER_MISSING, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 对象参数异常
     * @description: controller方法中，对象作为参数的校验
     * @author: 白剑民
     * @date: 2022-07-08 19:10:28
     * @return: top.dabaibai.core.response.Response
     * @version: 1.0
     */
    @SuppressWarnings("all")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DbbResponse<?> validEntity(MethodArgumentNotValidException e) {
        Map<String, String> collect = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        String msg = String.join(",", collect.values());
        return DbbResponse.fail(msg, SystemErrorCode.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param i 非法参数异常
     * @description:
     * @author: 白剑民
     * @date: 2023-08-05 17:19:22
     * @return: top.dabaibai.web.commons.http.DbbResponse<?>
     * @version: 1.0
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public DbbResponse<?> validArgument(IllegalArgumentException i) {
        return null;
    }

    /**
     * @param e 表单及普通参数异常
     * @description: controller方法中，表单及普通参数的校验
     * @author: 白剑民
     * @date: 2022-07-08 19:13:35
     * @return: top.dabaibai.core.response.Response
     * @version: 1.0
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public DbbResponse<?> validParam(ConstraintViolationException e) {
        Map<String, Object> collect = new HashMap<>(16);
        e.getConstraintViolations().forEach(c -> {
            PathImpl propertyPath = (PathImpl) c.getPropertyPath();
            NodeImpl leafNode = propertyPath.getLeafNode();
            String name = leafNode.getName();
            String value = c.getMessageTemplate();
            collect.put(name, value);
        });
        String msg = String.join(",", String.valueOf(collect.values()));
        return DbbResponse.fail(msg, SystemErrorCode.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 表单参数异常
     * @description: controller方法中：1、对象接收表单数据的校验；2、对象接收表单数据并做分组验参数据的校验
     * @author: 白剑民
     * @date: 2022-07-08 19:15:04
     * @return: top.dabaibai.core.response.Response
     * @version: 1.0
     */
    @ExceptionHandler(BindException.class)
    public DbbResponse<?> exceptionHandler(BindException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, String> collect = new HashMap<>(16);
        allErrors.forEach(error -> {
            FieldError fieldError = (FieldError) error;
            collect.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        String msg = String.join(",", collect.values());
        return DbbResponse.fail(msg, SystemErrorCode.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param e 自定义异常
     * @description: 处理自定义异常类
     * @author: 白剑民
     * @date: 2022-07-08 19:14:23
     * @return: java.lang.Object
     * @version: 1.0
     */
    @ExceptionHandler(value = DbbException.class)
    public DbbResponse<String> handleDbbException(DbbException e) {
        log.error("DbbException异常: {}", e.getDescribe());
        return DbbResponse.warn(null, e);
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        // 如果是封装过的，就直接返回
        if (body instanceof DbbResponse.ResponseBody) {
            return body;
        }
        // 如果没有封装过就封装一下，并返回body数据，给到下一层封装
        return DbbResponse.success(body).getBody();
    }
}
