## Dbb-Web 模块

#### 模块简介

```
此模块为Web项目开发依赖，集成或配置了部门常用的一些组件，使用此框架开发Web项目时，可仅引入starter依赖(内有框架常用组件子依赖)，也可按需引入
```

#### 包路径说明

- top.dabaibai.web：框架web开发starter依赖
- top.dabaibai.web.commons：web通用子模块
  - top.dabaibai.web.commons.http：统一响应、异常类
  - top.dabaibai.web.commons.model：通用实体类
- top.dabaibai.web.configuration：web通用配置子模块
  - top.dabaibai.web.configuration.annotations：通用注解
  - top.dabaibai.web.configuration.authcode：谷歌验证码
  - top.dabaibai.web.configuration.handler：全局处理器(异常处理器)
  - top.dabaibai.web.configuration.interceptor：全局拦截器(用户信息拦截器)
  - top.dabaibai.web.configuration.repeat：接口防重
  - top.dabaibai.web.configuration：web全局自动配置类
- top.dabaibai.web.validation：自定义参数校验注解实现

#### 模块功能示例

```xml
<dependency>
    <groupId>top.dabaibai</groupId>
    <artifactId>dbb-web-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

1、使用组合式控制层注解代替@RestController和@RestMapping

```java
@DbbController("/test")
public class TestController {
    
}
```