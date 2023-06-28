## Dbb-Code 模块

### 模块简介

```
此模块为框架核心工具模块，提供业务开发常用的工具，例如：文件上传进度条实现、全局用户信息获取等
```

#### 包路径说明

- top.dabaibai.core.enums：核心工具模块所使用或对外提供的常用枚举，如文件格式、excel文件类型等
- top.dabaibai.core.file：文件上传进度条实现
- top.dabaibai.core.pojo：通用对象
- top.dabaibai.core.utils：常用工具，如全局获取当前用户信息UserInfoUtils等
- top.dabaibai.core：包的父路径，通用常量及模块自动配置类

#### 模块集成

```xml
<dependency>
    <groupId>top.dabaibai</groupId>
    <artifactId>dbb-core</artifactId>
    <version>1.0.0</version>
</dependency>
```