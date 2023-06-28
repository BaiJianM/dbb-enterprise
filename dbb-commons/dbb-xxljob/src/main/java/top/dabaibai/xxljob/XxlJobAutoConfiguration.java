package top.dabaibai.xxljob;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: xxl-job 配置
 * @author: 白剑民
 * @dateTime: 2023-02-13 12:39:58
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties properties) {
        log.debug(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        XxlJobExecutor executor = properties.getExecutor();
        xxlJobSpringExecutor.setAdminAddresses(properties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(executor.getAppname());
        xxlJobSpringExecutor.setAddress(executor.getAddress());
        xxlJobSpringExecutor.setIp(executor.getIp());
        int tryPort = executor.getPort();
        // 判断是否允许系统自检端口，当用户设置端口时将忽略
        if (executor.isPortInUse() && tryPort == 0) {
            tryPort = 9999;
            // 判断端口是否已被占用，如果被占用就+1继续判断并创建。因为端口已被占用，xxl-job会根据9999加1作为端口进行寻址
            while (NetUtil.isPortUsed(tryPort)) {
                // 如果已有端口占用就+1继续判断
                tryPort += 1;
            }
        } else {
            log.warn("未经检查的xxl-job端口: {}，请留意是否存在端口占用", tryPort);
        }
        log.info("xxl-job实际启动端口: {}", tryPort);
        xxlJobSpringExecutor.setPort(tryPort);
        xxlJobSpringExecutor.setAccessToken(properties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(executor.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */


}