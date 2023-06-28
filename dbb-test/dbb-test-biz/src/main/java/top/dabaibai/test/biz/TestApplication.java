package top.dabaibai.test.biz;

// import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
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
@MapperScan("top.dabaibai.test.biz.mapper")
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
// @EnableAutoDataSourceProxy
@EnableFeignClients(basePackages = "top.dabaibai.user.api")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
