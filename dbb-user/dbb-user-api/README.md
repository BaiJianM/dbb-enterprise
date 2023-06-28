## Gientech-user-api模块使用手册

#### 注意事项

```
由于用户中心模块负责提供全局的用户信息、系统基础信息等实现，故在引入gientech-log模块时重新实现了ILogService接口，
使用Mysql作为操作日志存储实现。在com.gientech.iot.user.api.interfaces.IOperationLogService类中重写并调用了
gientech-user-biz模块的日志生成接口。若在引用本模块时，不希望将操作日志记录到用户中心的数据库中，
可重写gientech-log-core模块中的ILogService方法，将Bean交由Spring容器管理，并标注@Primary以覆盖其原实现方法即可。
```

#### 示例

```java
// 在引入了gientech-user-api的模块中创建LogConfiguration配置类
@Slf4j
@Configuration
public class LogConfiguration {

    @Bean
    @Primary
    public ILogService logService() {
        return (logDTO) -> {
            log.info("重写操作日志存储实现，接收到操作日志: {}", logDTO);
            // 存储逻辑
        };
    }

}
```

