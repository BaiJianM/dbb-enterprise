package top.dabaibai.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: GRPC客户端启动类
 * @author: 白剑民
 * @dateTime: 2023/9/1 15:07
 */
@SpringBootApplication
public class GrpcClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrpcClientApplication.class, args);
    }
}
