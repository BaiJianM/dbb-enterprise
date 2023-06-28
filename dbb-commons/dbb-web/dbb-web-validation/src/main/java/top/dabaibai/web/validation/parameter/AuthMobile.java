package top.dabaibai.web.validation.parameter;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @description: 手机号校验注解
 * @author: 白剑民
 * @dateTime: 2023/2/20 14:40
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MobileValidator.class)
public @interface AuthMobile {
    /**
     * 是否允许为空
     */
    boolean required() default true;

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "手机号格式不正确";

    /**
     * Constraint要求的属性，用于分组校验和扩展，默认留空
     */
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
