## RabbitMQ 安装部署手册

### [官方文档](https://www.rabbitmq.com/documentation.html)

### 安装部署

#### 1、环境准备

```
64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac
64 bit JDK 1.8+
```

#### 2、创建文件目录

```
mkdir -p /opt/docker/rabbitmq/data
```

#### 3、编写docker-compose.yml

```yml
version: '3'

networks:
  dbb:

services:
  # RabbitMQ服务
  rabbitmq:
    image: rabbitmq:management
    container_name: rabbitmq
    ports:
      # web控制台
      - 15672:15672
      # 服务端口
      - 5672:5672
    restart: always
    volumes:
      # 数据文件路径
      - ./rabbitmq/data:/var/lib/rabbitmq
    environment:
      - RABBITMQ_DEFAULT_VHOST=/
      # 用户名
      - RABBITMQ_DEFAULT_USER=admin
      # 密码
      - RABBITMQ_DEFAULT_PASS=123456
    networks:
      - dbb
```

#### 4、服务启动及其他操作（基于docker-compose）

##### 服务启动

```
docker-compose up -d rabbitmq
```

##### 服务停止

```
docker-compose stop rabbitmq
```

##### 服务状态查看

```
docker-compose ps rabbitmq
```

##### 日志查看

```
docker-compose logs -f rabbitmq
```

##### 进入容器

```
docker-compose exec rabbitmq /bin/bash
```

##### 容器删除

```
docker-compose rm rabbitmq
```