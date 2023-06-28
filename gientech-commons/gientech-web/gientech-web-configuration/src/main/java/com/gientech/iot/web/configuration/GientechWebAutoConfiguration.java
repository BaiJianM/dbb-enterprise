package com.gientech.iot.web.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gientech.iot.core.utils.SpringContextUtils;
import com.gientech.iot.redis.utils.RedisUtils;
import com.gientech.iot.web.configuration.handler.CommonExtHandler;
import com.gientech.iot.web.configuration.interceptor.CustomHandlerInterceptor;
import com.gientech.iot.web.configuration.repeat.RepeatRequestAspect;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description: web自动配置类
 * @author: 白剑民
 * @dateTime: 2022/10/12 15:59
 */
@Configuration
@Import({CommonExtHandler.class, SpringContextUtils.class})
public class GientechWebAutoConfiguration {

    /**
     * @description: 使用Jackson将控制层返回的Long型数据转换成String类型，避免精度丢失
     * @author: 白剑民
     * @date: 2022-10-12 16:10:25
     * @return: org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
     * @version: 1.0
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return j -> j.serializerByType(Long.class, new ToStringSerializer());
    }

    /**
     * @description: 自定义日期序列化方法
     * @author: 白剑民
     * @date: 2023-05-25 15:57:52
     * @return: org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
     * @version: 1.0
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer timeFormatCustomizer() {
        return j -> {
            ObjectMapper objectMapper = j.build();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                    String dateString = parser.readValueAs(String.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return LocalDateTime.parse(dateString, formatter);
                }
            });
            module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                    String dateString = parser.readValueAs(String.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(dateString, formatter);
                }
            });
            objectMapper.registerModule(module);
        };
    }

    /**
     * @description: spring security密码加密策略
     * @author: 白剑民
     * @date: 2023-05-23 09:41:33
     * @return: org.springframework.security.crypto.password.PasswordEncoder
     * @version: 1.0
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @param redisUtils redis工具
     * @description: 接口防重
     * @author: 白剑民
     * @date: 2023-05-23 09:42:26
     * @return: com.gientech.iot.core.annotations.repeat.RepeatRequestAspect
     * @version: 1.0
     */
    @Bean
    public RepeatRequestAspect repeatRequestAspect(RedisUtils redisUtils) {
        return new RepeatRequestAspect(redisUtils);
    }

    /**
     * @param redisUtils redis工具
     * @description: 添加自定义请求拦截器
     * @author: 白剑民
     * @date: 2023-06-05 18:18:49
     * @return: WebMvcConfigurer
     * @version: 1.0
     */
    @Bean
    public WebMvcConfigurer customMvcConfigurer(RedisUtils redisUtils) {
        return new CustomWebMvcConfigurer(redisUtils);
    }

    /**
     * @description: webmvc配置
     * @author: 白剑民
     * @dateTime: 2023-06-05 18:17:49
     */
    private static class CustomWebMvcConfigurer implements WebMvcConfigurer {

        private final RedisUtils redisUtils;

        public CustomWebMvcConfigurer(RedisUtils redisUtils) {
            this.redisUtils = redisUtils;
        }

        /**
         * @param registry 注册表
         * @description: 添加拦截器
         * @author: 王强
         * @date: 2023-02-14 17:26:00
         * @return: void
         * @version: 1.0
         */
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new CustomHandlerInterceptor(redisUtils))
                    .addPathPatterns("/**")
                    .excludePathPatterns("/", "/css/**", "fonts/**", "/images/**", "/js/**");
        }

    }
}
