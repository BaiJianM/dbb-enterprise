## Dbb-Feign 模块

#### 模块简介

```
本模块为feign远程接口调用客户端，使用OkHttpClient替换默认的URLConnection
```

#### 包路径说明

- top.dabaibai.feign：feign客户端拦截器及自动配置

#### 模块功能示例

```xml
<dependency>
    <groupId>top.dabaibai</groupId>
    <artifactId>dbb-feign</artifactId>
    <version>1.0.0</version>
</dependency>
```

1、请求拦截

```java
// 实现feign自带的RequestInterceptor接口中的apply方法
public class FeignRequestInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate template) {
    // 获取当前请求的 RequestAttributes
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes != null) {
      // 从 RequestAttributes 中获取请求头信息
      String headerValue = (String) requestAttributes.getAttribute("USER_INFO", RequestAttributes.SCOPE_REQUEST);
      if (headerValue != null) {
        // 添加自定义请求头到 Feign 请求模板中
        template.header("USER_INFO", headerValue);
      }
    }
  }
}
```

2、响应拦截

```java
// 通过top.dabaibai.feign.interceptor包下的FeignResponseInterceptor接口中的handle方法实现
// 结合部门所有项目来看用不到多个响应拦截，单个项目仅允许存在一个响应拦截实例，且是一个有效的Bean
@Component
public class CustomFeignResponseInterceptor implements FeignResponseInterceptor {
  @Override
  public void handle(Request request, Response response) {
    // 拦截逻辑
  }
}
```