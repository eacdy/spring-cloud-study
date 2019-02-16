package com.itmuch.cloud.study.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.itmuch.cloud.study.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * @author zhouli
 */
@RequestMapping("/movies")
@RestController
@Slf4j
public class MovieController {
  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/users/{id}")
  @SentinelResource(value = "findById", blockHandler = "exceptionHandler")
  public User findById(@PathVariable Long id) {
    // 这里用到了RestTemplate的占位符能力
    User user = this.restTemplate.getForObject(
        "http://microservice-provider-user/users/{id}",
        User.class,
        id
    );
    // ...电影微服务的业务...
    return user;
  }

  public User exceptionHandler(Long id, BlockException ex) {
    log.error("限流处理", ex);
    return new User(-1L, "默认用户", "默认用户", 1, new BigDecimal("1"));
  }
}
