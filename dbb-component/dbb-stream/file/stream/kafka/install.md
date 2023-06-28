## Kafka 安装部署手册

### [官方文档](https://kafka.apache.org/documentation/)

### 安装部署

#### 1、环境准备

```
64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac
```

#### 2、创建文件目录

```
mkdir -p /opt/docker/kafka-eagle
```

```
将项目`gientech-stream`模块`/file/kafka/`目录中的`system-config.properties`拷贝到`/opt/docker/kafka-eagle`下
```

#### 3、编写docker-compose.yml

```yml
version: '3'

networks:
  gientech:

services:
  # ----- Kafka服务(起)
  zookeeper:
    image: confluentinc/cp-zookeeper:6.1.1
    container_name: zookeeper
    ports:
      - 2181:2181
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      KAFKA_JMX_PORT: 39999
  kafka:
    image: confluentinc/cp-kafka:6.1.1
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - 9092:9092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      # 修改为对应的IP
      KAFKA_ADVERTISED_LISTENERS: 'PLAINTEXT://47.116.12.196:9092'
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_JMX_PORT: 49999
  kafka-eagle:
      image: nickzurich/efak:3.0.1
      container_name: kafka-eagle
      volumes: # 挂载目录
        - ./kafka-eagle/system-config.properties:/opt/efak/conf/system-config.properties
      environment: # 配置参数
        EFAK_CLUSTER_ZK_LIST: zookeeper:2181
      depends_on:
        - kafka
      ports:
        - 8048:8048
  # ----- Kafka服务(止)
```

#### 4、服务启动及其他操作（基于docker-compose）

##### 服务启动

```
docker-compose up -d zookeeper kafka kafka-eagle
```

##### 服务停止

```
docker-compose stop zookeeper kafka kafka-eagle
```

##### 服务状态查看

```
docker-compose ps zookeeper kafka kafka-eagle
```

##### 日志查看

```
docker-compose logs -f （对应服务名）
```

##### 进入容器

```
docker-compose exec （对应服务名） /bin/bash
```

##### 容器删除

```
docker-compose rm zookeeper kafka kafka-eagle
```

