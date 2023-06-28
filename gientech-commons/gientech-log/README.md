## Gientech-Log 模块使用手册

#### 模块简介

```
本模块旨在提供应用系统中操作日志和系统日志存储的解决方案。
1、系统日志，通过配置log4j2.xml文件，制定通用系统日志格式，便于日志收集插件进行收集并展示。
2、操作日志，为解决硬编码操作日志带来的不便维护的问题，通过自定义注解@OperationLog与@LogDiff配合Spring的Spell表达式，再通过带有Spell函数解析器的ThreadLocal类LogContext实现业务代码层面向注解注入属性，从而达到操作日志生成与业务代码完成解耦的目的。
```

#### 包路径说明

- com.gientech.iot.log.annotations：日志相关注解
- com.gientech.iot.log.core：系统日志及自定义操作日志实现、模块自动配置类
  - com.gientech.iot.log.core.aop：操作日志切面拦截器
  - com.gientech.iot.log.core.context：操作日志上下文
  - com.gientech.iot.log.core.enums：日志类型枚举
  - com.gientech.iot.log.core.function：操作日志处理函数
  - com.gientech.iot.log.core.pojo：操作日志相关对象
  - com.gientech.iot.log.core.properties：日志自定义配置
  - com.gientech.iot.log.core.service：操作日志存储扩展接口及默认实现

#### 模块集成

1、本模块提供最小化引入，如仅希望使用注解（例如在各应用模块的api子模块中），则只需引入gientech-log-annotations即可

```xml
<dependency>
    <groupId>com.gientech.iot</groupId>
    <artifactId>gientech-log-annotations</artifactId>
    <version>1.0.0</version>
</dependency>
```

2、若希望使用本模块所有功能，则需引入gientech-log-core

```xml
<dependency>
    <groupId>com.gientech.iot</groupId>
    <artifactId>gientech-log-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

3、注：此外，需注意，模块本身的操作日志存储默认实现为本地存储，若使用默认实现，则将会在项目跟路径下生成操作日志文件，名为"OperationLog.json"。如希望使用mysql等三方存储，请实现`com.gientech.iot.log.core.service.ILogService`接口


#### 模块功能示例

1、操作日志（普通使用方法）

```java
// 若业务模块引入了框架自带的gientech-user-api子模块，则无需实现ILogService接口
// gientech-user-api子模块中的IOperationLogService接口将使业务模块的日志记录到user模块数据库中
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "用户信息相关接口")
@Validated
@Slf4j
public class UserController {
  // 以下代码解释
  // LogContext.putVariables() 为日志上下文添加Spel要解析的变量
  // @OperationLog中标注了'SpEL表达式'注释的参数都可使用Spel表达式填写
  @OperationLog(bizEvent = "#event",
          msg = "'【' + #user.realName + '】成功退出【' + #system.systemName + '】系统'",
          operatorId = "#user.userId", operatorCode = "#user.username", operatorName = "#user.realName",
          tag = "#tag")
  @Operation(summary = "注销成功", hidden = true)
  @GetMapping("/logoutSuccess")
  public void logoutSuccess() {
    BaseUserInfoVO userInfo = UserInfoUtils.getUserInfo();
    SysSystem systemInfo = systemService.getById(UserInfoUtils.getSystemId());
    LogContext.putVariables("event", LoginTypeEnum.LOGOUT.getCode());
    LogContext.putVariables("tag", LogTypeEnum.LOGIN_LOG.getCode());
    LogContext.putVariables("user", userInfo);
    LogContext.putVariables("system", systemInfo);
  }
}
```

2、操作日志（进阶使用方法）

```java
// 多组操作日志记录，可使用@OperationLogs
@OperationLogs({
        @OperationLog(),
        @OperationLog(),
})
```