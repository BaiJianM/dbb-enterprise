package com.gientech.iot.web.validation.parameter;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 手机号注解校验器
 * @author: 白剑民
 * @dateTime: 2023/2/20 14:40
 */
public class MobileValidator implements ConstraintValidator<AuthMobile, CharSequence> {

    private boolean required = false;

    /**
     * 验证手机号正则
     */
    private final Pattern pattern = Pattern.compile("^1[34578][0-9]{9}$");

    /**
     * @param constraintAnnotation 校验注解
     * @description: 在验证开始前调用注解里的方法，从而获取到一些注解里的参数
     * @author: 白剑民
     * @date: 2023-02-20 14:43:29
     * @version: 1.0
     */
    @Override
    public void initialize(AuthMobile constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    /**
     * @param value   待校验值
     * @param context 校验上下文
     * @description: 判断参数是否合法
     * @author: 白剑民
     * @date: 2023-02-20 14:43:56
     * @return: boolean
     * @version: 1.0
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (this.required) {
            // 验证
            return isMobile(value);
        }
        if (StringUtils.hasText(value)) {
            // 验证
            return isMobile(value);
        }
        return true;
    }

    /**
     * @param str 待校验手机号字符串
     * @description: 正则校验手机号字符串
     * @author: 白剑民
     * @date: 2023-02-20 14:45:37
     * @return: boolean
     * @version: 1.0
     */
    private boolean isMobile(final CharSequence str) {
        Matcher m = pattern.matcher(str);
        return m.matches();
    }
}
