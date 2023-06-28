## Gientech-Database 模块

### 模块简介

```
此模块为持久层框架实现，当前ORM框架为mybatis-plus，内置多种拦截器用以实现基础的数据权限及主从、读写分离等
```

#### 包路径说明

- com.gientech.iot.database.annotation：模块注解，当前仅实现数据权限、mysql悲观锁
- com.gientech.iot.database.datapermission：数据权限简单实现
- com.gientech.iot.database.entity：基础实体类，需业务表继承以实现自动填充
- com.gientech.iot.database.enums：模块内置枚举
- com.gientech.iot.database.handler：全局处理器，目前实现基础实体类数据自动填充处理器
- com.gientech.iot.database.interceptor：全局拦截器，目前实现读写分离、悲观锁拦截器
- com.gientech.iot.database.properties：模块自定义配置，目前可配置多数据源相关
- com.gientech.iot.database：包的父路径，模块自动配置类

#### 模块功能示例

```xml
<dependency>
    <groupId>com.gientech.iot</groupId>
    <artifactId>gientech-database</artifactId>
    <version>1.0.0</version>
</dependency>
```

1、乐观锁

```java
// 通过继承实体基类BaseEntity，其内置的字段上的@Version注解实现
```

2、悲观锁

```java
// 通过@PessimisticLockInterceptor注解实现，将该注解标于mapper类的接口之上，会在sql的末尾拼接for update
@Repository
public interface DoTestMapper extends BaseMapper<DoTest> {

    // 通过重写BaseMapper类默认的查询列表方法，将DoTestMapper的selectList方法以悲观锁方式查询
    @Override
    @PessimisticLockInterceptor(forUpdate = true)
    List<DoTest> selectList(Wrapper<DoTest> queryWrapper);

}
```

3、多数据源及读写分离

```java
// 与官方使用相同，仅做基础配置，未做调整。可查看demo模块相应实示例
```