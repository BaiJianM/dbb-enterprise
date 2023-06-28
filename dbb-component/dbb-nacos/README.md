## SpringCloud Alibaba Nacos安装部署手册

### [官方文档](https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html)

### 安装部署

#### 1、环境准备

```
64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac
64 bit JDK 1.8+
```

#### 2、创建文件目录

```shell
mkdir -p /opt/docker/nacos/logs
```

```shell
mkdir -p /opt/docker/nacos/conf
```

```
将项目`dbb-nacos`模块`file`目录中的`/nacos/conf`底下的所有文件拷贝至`/opt/docker/nacos/conf`
- `nacos-logback.xml`可调整日志配置
- `application.properties`为`nacos`基础配置，可进行端口、外置数据库、日志生成开关等常用配置
- `schema.sql`为使用外置数据库配置时，用于建库的脚本
```



#### 3、编写docker-compose.yml

```yml
version: '3'

networks:
  dbb:

services:
  # Nacos服务
  nacos:
    image: nacos/nacos-server:1.4.2
    restart: always
    container_name: nacos
    environment:
      # 支持主机名可以使用hostname,否则使用ip，默认ip
      - PREFER_HOST_MODE=ip
      # 单机模式
      - MODE=standalone
    volumes:
      # 日志文件路径
      - ./nacos/logs:/home/nacos/logs
      # 配置文件路径
      - ./nacos/conf:/home/nacos/conf
    ports:
      - 8848:8848
    networks:
      - dbb
```



#### 4、服务启动及其他操作（基于docker-compose）

##### 服务启动

```
docker-compose up -d nacos
```

##### 服务停止

```
docker-compose stop nacos
```

##### 服务状态查看

```
docker-compose ps nacos
```

##### 日志查看

```
docker-compose logs -f nacos
```

##### 进入容器

```
docker-compose exec nacos /bin/bash
```

##### 容器删除

```
docker-compose rm nacos
```

