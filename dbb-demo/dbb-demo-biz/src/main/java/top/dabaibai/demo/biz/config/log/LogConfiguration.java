package top.dabaibai.demo.biz.config.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.dabaibai.log.core.service.ILogService;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/5/6 17:37
 */
@Slf4j
@Configuration
public class LogConfiguration {

    @Bean
    @Primary
    public ILogService logService() {
        return (logDTO) -> {
            log.info("新定义的东东");
        };
    }

}
