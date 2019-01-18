package com.itmuch.cloud.study.user.feign;

import com.itmuch.cloud.study.user.entity.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

/**
 * @author zhouli
 */
@FeignClient(name = "microservice-provider-user", fallbackFactory = UserFeignClientFallbackFactory.class)
public interface UserFeignClient {
  @GetMapping("/users/{id}")
  User findById(@PathVariable("id") Long id);
}

@Component
@Slf4j
class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
  @Override
  public UserFeignClient create(Throwable throwable) {
    return new UserFeignClient() {
      @Override
      public User findById(Long id) {
        log.error("进入回退逻辑", throwable);
        return new User(id, "默认用户", "默认用户", 0, new BigDecimal(1));
      }
    };
  }
}
