## Gientech-Redis 模块

#### 模块简介

```
此模块为分布式缓存Redis依赖，提供Redis常用操作工具及广播通知实现。同时，可通过配置开启Spring Cache集成，将Redis作为Spring Cache的实现
```

#### 包路径说明

- com.gientech.iot.redis.annotation：缓存模块通用注解
- com.gientech.iot.redis.listener：广播通知实现
- com.gientech.iot.redis.properties：自定义配置
- com.gientech.iot.redis.utils：缓存工具类
- com.gientech.iot.redis：包父路径，模块自动配置类 