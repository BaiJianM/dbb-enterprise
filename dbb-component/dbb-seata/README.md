## Spring Cloud Alibaba Seata 安装部署手册

### [官方文档](http://seata.io/zh-cn/docs/overview/what-is-seata.html)



### 安装部署

#### 1、环境准备

```
64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac
```

#### 2、创建文件目录

```shell
mkdir -p /opt/docker/seata-server/conf
```

```
将项目`dbb-seata`模块`file`目录中的`/seata`底下的所有文件拷贝至`/opt/docker/seata-server/conf`
- `registry.conf`为配置文件，本框架中搭配`nacos`使用，其中需配置`nacos`信息
- `seata.sql`为建库脚本
```

#### 3、编写docker-compose.yml

```yml
version: '3'

networks:
  dbb:

services:
  # 分布式事务中心服务
  seata-server:
    image: seataio/seata-server:1.4.2
    container_name: seata-server
    hostname: seata-server
    ports:
      - "8091:8091"
    environment:
      # 指定seata服务启动端口
      - SEATA_PORT=8091
      # 注册到nacos上的ip。客户端将通过该ip访问seata服务。
      # 注意公网ip和内网ip的差异。
      - SEATA_IP=47.116.12.196
      - SEATA_CONFIG_NAME=file:/root/seata-config/registry
    volumes:
    # 因为registry.conf中是nacos配置中心，只需要把registry.conf放到./seata-server/conf文件夹中
      - ./seata-server/conf:/root/seata-config/conf
```

#### 4、Nacos端与引入模块的配置

```
1、创建seata单独使用的命名空间
2、将项目`dbb-seata`模块`file`目录中的`/seata`底下的`seata_nacos_config.zip`导入seata命名空间的配置列表中
```

```yml
# 在引用了seata模块服务的配置文件中加入以下配置
# 分布式事务seata服务
seata:
  data-source-proxy-mode: AT
  # 对应nacos配置列表中Data Id为service.vgroupMapping.demo_tx_group的第三部分后缀
  tx-service-group: demo_tx_group
  config:
    type: nacos
    nacos:
      # nacos配置列表中对应的Data Id
      data-id: seata-server.yml
      group: SEATA_GROUP
      # 单独为seata配置的命名空间id
      namespace: 588cbf89-9d4a-4fca-a041-df08ecd139de
      server-addr: 124.223.178.24:8848
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 124.223.178.24:8848
      group: "SEATA_GROUP"
      # 单独为seata配置的命名空间id
      namespace: 588cbf89-9d4a-4fca-a041-df08ecd139de
```



#### 5、服务启动及其他操作（基于docker-compose）

##### 服务启动

```
docker-compose up -d seata-server
```

##### 服务停止

```
docker-compose stop seata-server
```

##### 服务状态查看

```
docker-compose ps seata-server
```

##### 日志查看

```
docker-compose logs -f seata-server
```

##### 进入容器

```
docker-compose exec seata-server /bin/bash
```

##### 容器删除

```
docker-compose rm seata-server
```
