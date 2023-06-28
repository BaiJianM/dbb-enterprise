## RocketMQ 安装部署手册

### [官方文档](https://rocketmq.apache.org/zh/docs/4.x/)

### 安装部署

#### 1、环境准备

```
64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac
64 bit JDK 1.8+
```

#### 2、创建文件目录

```
mkdir -p /opt/docker/rocketmq/broker1/store
mkdir -p /opt/docker/rocketmq/broker1/logs
mkdir -p /opt/docker/rocketmq/broker1/conf
mkdir -p /opt/docker/rocketmq/namesrv/logs
```

```
将项目`dbb-stream`模块`/file/rocketmq/`目录中的`broker.conf`拷贝到`/opt/docker/rocketmq/broker1/conf`下
```

#### 3、编写docker-compose.yml

```yml
version: '3'

networks:
  dbb:

services:
  # ----- RocketMQ服务(起)
  # NameServer服务
  rocketmq_namesrv:
    image: apache/rocketmq:4.9.2
    container_name: rocketmq_namesrv
    ports:
      - 9876:9876
    volumes:
      - ./rocketmq/namesrv/logs:/home/rocketmq/logs
    command: sh mqnamesrv

  # Broker节点1服务
  rocketmq_broker:
    image: apache/rocketmq:4.9.2
    container_name: rocketmq_broker
    ports:
      - 10909:10909
      - 10911:10911
      - 10912:10912
    links:
      - rocketmq_namesrv
    volumes:
      - ./rocketmq/broker1/logs:/home/rocketmq/logs
      - ./rocketmq/broker1/store:/home/rocketmq/store
      - ./rocketmq/broker1/conf/broker.conf:/home/rocketmq/rocketmq-4.9.2/conf/broker.conf
    environment:
      NAMESRV_ADDR: "rocketmq_namesrv:9876"
      JAVA_OPTS: " -Duser.home=/opt"
      JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: sh mqbroker -n rocketmq_namesrv:9876 -c ../conf/broker.conf
    depends_on:
      - rocketmq_namesrv

  # RocketMQ可视化服务
  rocketmq_dashboard:
    image: apacherocketmq/rocketmq-dashboard:latest
    container_name: rocketmq_dashboard
    ports:
      - 8868:8080
    environment:
        JAVA_OPTS: "-Xms128m -Xmx128m -Xmn128m -Drocketmq.namesrv.addr=rocketmq_namesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false"
    depends_on:
      - rocketmq_namesrv
  # ----- RocketMQ服务(止)
```

#### 4、服务启动及其他操作（基于docker-compose）

##### 服务启动

```
docker-compose up -d rocketmq_namesrv rocketmq_broker rocketmq_dashboard
```

##### 服务停止

```
docker-compose stop rocketmq_namesrv rocketmq_broker rocketmq_dashboard
```

##### 服务状态查看

```
docker-compose ps rocketmq_namesrv rocketmq_broker rocketmq_dashboard
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
docker-compose rm rocketmq_namesrv rocketmq_broker rocketmq_dashboard
```
