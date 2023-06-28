## Gientech-Code 模块

### 模块简介

```
此模块为框架核心工具模块，提供业务开发常用的工具，例如：文件上传进度条实现、全局用户信息获取等
```

#### 包路径说明

- com.gientech.iot.core.enums：核心工具模块所使用或对外提供的常用枚举，如文件格式、excel文件类型等
- com.gientech.iot.core.file：文件上传进度条实现
- com.gientech.iot.core.pojo：通用对象
- com.gientech.iot.core.utils：常用工具，如全局获取当前用户信息UserInfoUtils等
- com.gientech.iot.core：包的父路径，通用常量及模块自动配置类

#### 模块集成

```xml
<dependency>
    <groupId>com.gientech.iot</groupId>
    <artifactId>gientech-core</artifactId>
    <version>1.0.0</version>
</dependency>
```