## Spring Cloud Alibaba Sentinel 安装部署手册



### [官方文档](https://sentinelguard.io/zh-cn/docs/introduction.html)



### 安装部署



#### 1、环境准备

```
64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac
64 bit JDK 1.8+
```

#### 2、创建文件目录

```shell
mkdir -p /opt/docker/sentinel-dashboard
```

```
将项目`dbb-sentinel`模块`file`目录中的`/sentinel-dashboard`底下的所有文件拷贝至`/opt/docker/sentinel-dashboard`
- `Dockerfile`docker启动脚本
- `sentinel-dashboard-1.8.3.jar`可视化项目包
```

#### 3、编写docker-compose.yml

```yml
version: '3'

networks:
  dbb:

services:
  # sentinel-dashboard服务
  sentinel-dashboard:
    container_name: sentinel-dashboard
    restart: always
    build:
      context: ./sentinel-dashboard
      dockerfile: Dockerfile
    ports:
      - "8858:8858"
    networks:
      - dbb
```

#### 4、服务启动及其他操作（基于docker-compose）

##### 服务启动

```
docker-compose up -d sentinel-dashboard
```

##### 服务停止

```
docker-compose stop sentinel-dashboard
```

##### 服务状态查看

```
docker-compose ps sentinel-dashboard
```

##### 日志查看

```
docker-compose logs -f sentinel-dashboard
```

##### 进入容器

```
docker-compose exec sentinel-dashboard /bin/bash
```

##### 容器删除

```
docker-compose rm sentinel-dashboard
```
