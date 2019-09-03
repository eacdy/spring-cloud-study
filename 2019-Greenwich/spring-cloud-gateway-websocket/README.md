# README

> **TIPS**
>
> * 本文基于Spring Cloud Greenwich SR2，理论支持Spring Cloud Finchley及更高版本。
> * 部分WebSocket相关代码来自：`https://www.jb51.net/article/145659.htm` ，鸣谢！

最近有朋友问到我Spring Cloud Gateway实现WebSocket代理的问题。索性做了个示例项目。



## 使用说明

* 启动 `websocket-service` 

* 启动 `gateway`

* 访问 `http://localhost:8040` ，输入提示，观察浏览器Console。

  ![image-20190903135723672](/Users/reno/Library/Application Support/typora-user-images/image-20190903135723672.png)

  

## 核心代码

### WebSocket微服务

- 加依赖：

  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
  </dependency>
  ```

- 代码

  ```java
  @Configuration
  @EnableWebSocket
  public class WebSocketConfig implements WebSocketConfigurer {
      @Override
      public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
          registry.addHandler(myHandler(), "test-ws/{id}")
                  // 设置允许跨域访问
                  .setAllowedOrigins("*");
      }
  
      public WebSocketHandler myHandler() {
          return new MyHandler();
      }
  
  }
  
  class MyHandler extends TextWebSocketHandler {
      @Override
      protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
          String payload = message.getPayload();
          System.out.println("接受到的数据" + payload);
          session.sendMessage(new TextMessage("服务器返回收到的信息," + payload));
      }
  }
  ```

- 配置

  ```yaml
  server:
    port: 8080
  ```

  

### Gateway

* 依赖

  ```xml
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
  </dependency>
  ```

* 配置

  ```yaml
  spring:
    cloud:
      gateway:
        routes:
          - id: aaa
            uri: ws://localhost:8080
            predicates:
            - Path=/test-ws/**
  server:
    port: 8040
  ```

  **注**：如果你希望让Gateway自动从该服务发现组件上获取微服务，则可将uri配置成 `lb:ws://微服务在服务发现组件上的名称` 

* 前端网页：

  ```html
  <!DOCTYPE html>
  <html>
  <head>
      <meta charset="utf-8"/>
      <title></title>
  </head>
  <body>
  <input id="text" type="text"/>
  <button onclick="send()">Send</button>
  <button onclick="closeWebSocket()">Close</button>
  <div id="message">
  </div>
  <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
  <script>
  
      var userID = "888";
      var websocket = null;
  
      $(function () {
          connectWebSocket();
      })
  
      //建立WebSocket连接
      function connectWebSocket() {
          console.log("开始...");
  
          //建立webSocket连接
          websocket = new WebSocket("ws://127.0.0.1:8040/test-ws/" + userID);
  
          //打开webSokcet连接时，回调该函数
          websocket.onopen = function () {
              console.log("onpen");
          }
  
          //关闭webSocket连接时，回调该函数
          websocket.onclose = function () {
              //关闭连接
              console.log("onclose");
          }
  
          //接收信息
          websocket.onmessage = function (msg) {
              console.log(msg.data);
          }
      }
  
      //发送消息
      function send() {
          var postValue = {};
          postValue.id = userID;
          postValue.message = $("#text").val();
          websocket.send(JSON.stringify(postValue));
      }
  
      //关闭连接
      function closeWebSocket() {
          if (websocket != null) {
              websocket.close();
          }
      }
  </script>
  </body>
  </html>
  ```

  

