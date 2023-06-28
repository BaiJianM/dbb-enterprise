package com.gientech.iot.web.configuration.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @description: 组合式Web控制层注解
 * @author: 白剑民
 * @dateTime: 2023/06/09 19:33
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface GientechController {
    /**
     * @description: 统一请求路径
     * @author: 白剑民
     * @date: 2023-06-09 19:33:09
     * @return: java.lang.String[]
     * @version: 1.0
     */
    @AliasFor("path")
    String[] value() default {};

    /**
     * @description: 统一请求路径
     * @author: 白剑民
     * @date: 2023-06-09 19:33:09
     * @return: java.lang.String[]
     * @version: 1.0
     */
    @AliasFor("value")
    String[] path() default {};
}
