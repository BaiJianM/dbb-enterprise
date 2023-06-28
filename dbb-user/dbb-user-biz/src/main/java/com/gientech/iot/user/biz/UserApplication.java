package com.gientech.iot.user.biz;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @description: 用户中心启动类
 * @author: 白剑民
 * @dateTime: 2022/10/17 15:46
 */
@MapperScan("com.gientech.iot.user.biz.mapper")
@SpringBootApplication(exclude = DynamicDataSourceAutoConfiguration.class)
@EnableCaching
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
