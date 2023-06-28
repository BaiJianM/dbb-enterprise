## Gientech-Stream 模块使用手册

### [Spring Cloud Stream 官方文档](https://docs.spring.io/spring-cloud-stream/docs/3.2.2/reference/html/spring-cloud-stream.html#spring-cloud-stream-reference)

### 模块简介

```
1、基于`Spring Cloud Stream`封装，提供符合开发日常使用习惯的消息队列工具
2、在消息收发上做了一定的封装实现，藉由`Spring Cloud Stream`的屏蔽不同消息队列底层实现的思想，使模块使用者无需关心三种消息队列的基本差异，仅需实现模块提供的部分接口，就能够做到基本消息的收发
3、基于Netty时间轮算法实现类HashedWheelTimer，提供统一化的秒级延时消息实现（使消息隔离于三种消息队列之前）
4、提供三种消息队列基于`Spring Cloud Stream`的最佳实践配置
5、目前暂仅提供`RocketMQ`、`RabbitMQ`及`Kafka`的封装实现
```

### 消息队列中间件安装部署

#### [RocketMQ](./file/stream/rocketmq/install.md)

#### [RabbitMQ](./file/stream/rabbitmq/install.md)

#### [Kafka](./file/stream/kafka/install.md)

### 接口说明

#### 消费者监听（GientechConsumerListener接口类与@GientechMQListener注解）

```
1、实现GientechConsumerListener接口中的onMessage方法可进行消息监听，允许多个实现
2、在实现了GientechConsumerListener接口的所有实现类上必须标注@GientechMQListener注解，并提供监听的tag标签，消息会经由tag进行分发路由
3、在常量类GientechMQConstant.Headers给出了差异化消息队列的特殊标签，可通过阅读Spring Cloud Stream官方文档进行不同消息队列的特殊消息实现
```

#### 统一化延时消息实现

```
1、com.gientech.iot.stream.producer.delay包下提供了延时消息的存储等功能实现，延时消息运行时默认存储于内存，并同步持久化未发送消息与发送失败的消息于本地缓存文件当中
2、对于延时消息的存储实现，调用者可通过实现DelayMessageStoreService接口的方法来达到不同方式的存储
注：
	1.因默认延时消息的存储由本地缓存文件实现，故而存在多节点下服务宕机导致的消息丢失情况
			一种可行的存储方案：可通过mysql主从同步实现消息的持久化
	2.因Netty实现的时间轮算法为单层时间轮，故而不建议将超过24小时的延时消息交由此模块实现
			一种可行的超长延时方案：通过xxl-job + 此模块来实现，思路如下：
			（1）由xxl-job来进行每天指定时间点（例如凌晨1点）对所有超长延时消息进行延时时间重算
			（2）一旦延时时间小于24小时的，放入此模块进行延时
```

#### 模块功能示例

```xml
<dependency>
    <groupId>com.gientech.iot</groupId>
    <artifactId>gientech-stream</artifactId>
    <version>1.0.0</version>
</dependency>
```

1、实现消息的发送和监听

```java
// 消息发送
// 以RocketMQ为例，以下代码除请求头区分RocketMQ事务消息外
// 即使切换消息中间件为Kafka和RabbitMQ，代码也无需更改
// 注意：tag为必填项，用以区分业务
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TestProducer {

    private final GientechProducer gientechProducer;

    // 异步带回调
    public void testStreamBridgeRocketMQ(String outputChannelName) {
        DoTest doTest = new DoTest();
        doTest.setName("测试RocketMQ消息发送");
        GientechMessage gientechMessage = GientechMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .build();
        try {
            gientechProducer.sendWithAsync(gientechMessage, () -> log.info("成功的回调"), () -> log.info("失败的回调"), outputChannelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 延时多条事务消息发送
    public void testStreamBridgeRocketTransaction() {
        DoTest doTest = new DoTest();
        doTest.setName("测试StreamBridgeRocketmq事务消息发送");
        GientechMessage gientechMessage = GientechMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .header(GientechMQConstant.Headers.TRANSACTIONAL_ARGS, "binder")
                .build();

        doTest.setName("测试StreamBridgeRocketmq事务消息发送-1");
        GientechMessage gientechMessage2 = GientechMessage.builder()
                .msg(JSON.toJSONString(doTest))
                .tag("tag1")
                .header(GientechMQConstant.Headers.TRANSACTIONAL_ARGS, "binder")
                .build();

        try {
            gientechProducer.sendWithAsync(gientechMessage, 3L, TimeUnit.SECONDS);
            gientechProducer.sendWithAsync(gientechMessage2, 10L, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(1);
            log.info("当前有 {} 任务正在排队", gientechProducer.getPendingMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 消息接收
// 使用@GientechMQListener注解与GientechConsumerListener接口搭配使用
// 这里的tag就是要监听的消息业务标识，为数组形式，可监听多个
@GientechMQListener(tags = {"tag1"})
public class TestConsumerListener implements GientechConsumerListener {

    @Override
    public void onMessage(GientechMessage message) {
        log.info("接收到消息: {}", message);
    }
}
```