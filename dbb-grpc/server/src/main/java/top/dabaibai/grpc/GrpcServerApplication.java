package top.dabaibai.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: GRPC服务端启动类
 * @author: 白剑民
 * @dateTime: 2023/9/1 15:08
 */
@SpringBootApplication
public class GrpcServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrpcServerApplication.class, args);
    }
}
