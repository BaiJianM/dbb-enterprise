package com.gientech.iot.demo.biz;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description: 测试模块启动类
 * @author: 白剑民
 * @dateTime: 2023/2/14 09:44
 */
@Slf4j
@MapperScan("com.gientech.iot.demo.biz.mapper")
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@EnableAutoDataSourceProxy
@EnableFeignClients(basePackages = {"com.gientech.iot.user.api", "com.gientech.iot.test.api"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
