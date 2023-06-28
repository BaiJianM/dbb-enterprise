Apache SkyWalking
==========

<img src="http://skywalking.apache.org/assets/logo.svg" alt="Sky Walking logo" height="90px" align="right" />

**SkyWalking**: APM（应用程序性能监控器）系统，专为微服务、云原生和基于容器的架构而设计.
- [官方文档](https://skywalking.apache.org/docs/#SkyWalking)

## 介绍
**SkyWalking** 是一个开源的APM系统，包括云原生架构中分布式系统的监控、追溯、诊断能力.

* 分布式跟踪
    * 端到端分布式跟踪。服务拓扑分析、以服务为中心的可观测性和 API 仪表板.
* 堆栈的代理
    * Java，.Net Core，PHP，NodeJS，Golang，LUA，Rust，C++，Client JavaScript和Python代理，具有积极的开发和维护.
* eBPF早期采用
    * Rover 代理充当由 eBPF 提供支持的指标收集器和分析器，以诊断 CPU 和网络性能.
* 伸缩
    * 可以从一个SkyWalking集群收集和分析100+十亿个遥测数据.
* 支持成熟的遥测生态系统
    * 支持来自成熟生态系统的指标、跟踪和日志，例如 Zipkin、OpenTelemetry、Prometheus、Zabbix、Fluentd
* 原生 APM 数据库
    * BanyanDB 是一个可观测性数据库，创建于 2022 年，旨在摄取、分析和存储遥测可观测性数据.
* 一致的指标聚合
    * SkyWalking原生仪表格式和广为人知的指标格式（OpenCensus，OTLP，Telegraf，Zabbix等）通过相同的脚本管道进行处理.
* 日志管理通道
    * 通过脚本流水线支持日志格式化、提取指标、各种采样策略，高性能.
* 警报和遥测通道
    * 支持以服务为中心、以部署为中心、以API为中心的告警规则设置。支持将告警和所有遥测数据转发到第三方.

<img src="https://skywalking.apache.org/images/home/architecture.svg?t=20220513"/>

## 运维部署
##### 创建文件目录

```shell
mkdir -p /opt/docker/skywalking/package
```

```
将项目`dbb-skywalking`模块`file`目录中的skywalking-agent.jar文件拷贝至`/opt/docker/skywalking/package`
- `Dockerfile`docker启动脚本
- `skywalking-agent.jar`拦截代理
```
##### 创建docker-compose文件
```
vim docker-compose.yml
```
```
version: '3.8'
services:
  skywalking-oap:
    image: apache/skywalking-oap-server:9.3.0
    container_name: skywalking-oap
    ports:
      - "11800:11800"
      - "12800:12800"
    healthcheck:
      test: [ "CMD-SHELL", "/skywalking/bin/swctl ch" ]
      interval: 30s
      timeout: 10s
      retries: 3
    volumes:
    # 存放拦截代理的jar包，用于需要进行链路追踪的服务进行启动参数配置
      - ./skywalking/package:/skywalking
    environment:
      SW_HEALTH_CHECKER: default
      JAVA_OPTS: "-Xms256m -Xmx256m"

  skywalking-ui:
    image: apache/skywalking-ui:9.3.0
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    links:
      - skywalking-oap
    ports:
      - "8081:8080"
    environment:
      SW_OAP_ADDRESS: http://skywalking-oap:12800
```

##### 直接启动查看日志
```
docker-compose -f ./docker-compose.yml up
```

##### 访问web控制台

```
http://localhost:8081
```

##### 后台启动
```
docker-compose up -d
```

##### 查看容器分配的IP
```
docker inspect 容器名或容器id
```

##### springboot项目启动
```
配置jvm参数 
-javaagent:[skywalking-agent.jar文件路径] 
-Dskywalking.agent.service_name=[项目在nacos注册的服务名] 
-Dskywalking.collector.backend_service=[skywalking服务地址] 
```