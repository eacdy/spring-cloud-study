# 项目简介
本项目是《使用Spring Cloud与Docker实战微服务》：

[http://git.oschina.net/itmuch/spring-cloud-book](http://git.oschina.net/itmuch/spring-cloud-book)

[http://www.github.com/eacdy/spring-cloud-book](http://www.github.com/eacdy/spring-cloud-book)

Spring Cloud章节的配套代码，如有疑问，请移步至该地址。

微服务架构交流QQ群：157525002（2000人群，已满），**564840207（2000人群，欢迎加入）**，欢迎加入。



内容主要包含：

| 微服务角色                 | 对应的技术选型                              |
| --------------------- | ------------------------------------ |
| 注册中心(Register Server) | Eureka                               |
| 服务提供者                 | spring mvc、spring-data-jpa、h2等       |
| 服务消费者                 | Ribbon/Feign消费服务提供者的接口               |
| 熔断器                   | Hystrix，包括Hystrix Dashboard以及Turbine |
| 配置服务                  | Spring Cloud Config Server           |
| API Gateway           | Zuul                                 |



# 准备

## 环境准备：

| 工具    | 版本或描述                |
| ----- | -------------------- |
| JDK   | 1.8                  |
| IDE   | STS 或者 IntelliJ IDEA |
| Maven | 3.x                  |

## 主机名配置：

| 主机名配置（C:\Windows\System32\drivers\etc\hosts文件） |
| ---------------------------------------- |
| 127.0.0.1 discovery config-server gateway movie user feign ribbon |

## 主机规划：

| 项目名称                                     | 端口   | 描述                     | URL             |
| ---------------------------------------- | ---- | ---------------------- | --------------- |
| microservice-api-gateway                 | 8050 | API Gateway            | 详见文章            |
| microservice-config-client               | 8041 | 配置服务的客户端               | 详见文章            |
| microservice-config-server               | 8040 | 配置服务                   | 详见文章            |
| microservice-consumer-movie-feign        | 8020 | Feign Demo             | /feign/1        |
| microservice-consumer-movie-feign-with-hystrix | 8021 | Feign Hystrix Demo     | /feign/1        |
| microservice-consumer-movie-feign-with-hystrix-stream | 8022 | Hystrix Dashboard Demo | /feign/1        |
| microservice-consumer-movie-ribbon       | 8010 | Ribbon Demo            | /ribbon/1       |
| microservice-consumer-movie-ribbon-with-hystrix | 8011 | Ribbon Hystrix Demo    | /ribbon/1       |
| microservice-discovery-eureka            | 8761 | 注册中心                   | /               |
| microservice-hystrix-dashboard           | 8030 | hystrix监控              | /hystrix.stream |
| microservice-hystrix-turbine             | 8031 | turbine                | /turbine.stream |
| microservice-provider-user               | 8000 | 服务提供者                  | /1              |
|                                          |      |                        |                 |





## 实体书《Spring Cloud与Docker微服务架构实战》上市啦！

* 京东购书地址：

![](jd-购书链接.png)

