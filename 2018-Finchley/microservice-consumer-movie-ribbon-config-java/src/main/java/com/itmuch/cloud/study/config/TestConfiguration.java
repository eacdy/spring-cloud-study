package com.itmuch.cloud.study.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;
import config.RibbonConfiguration;

/**
 * 使用RibbonClient，为特定的目标服务自定义配置。
 * 使用@RibbonClient的configuration属性，指定Ribbon的配置类。
 * 可参考的示例：
 * http://spring.io/guides/gs/client-side-load-balancing/
 *
 * @author 周立
 */
@Configuration
@RibbonClient(name = "microservice-provider-user", configuration = RibbonConfiguration.class)
public class TestConfiguration {
}
