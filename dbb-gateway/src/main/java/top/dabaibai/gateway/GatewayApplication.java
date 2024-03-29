package top.dabaibai.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description: Gateway网关启动类
 * @author: 白剑民
 * @dateTime: 2022-10-17 16:09:37
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.dabaibai.user.api")
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        // 集成sentinel-gateway时增加此行代码表示该服务为网关服务以显示"API管理"菜单
        System.setProperty("csp.sentinel.app.type", "1");
        SpringApplication.run(GatewayApplication.class, args);
    }

}
