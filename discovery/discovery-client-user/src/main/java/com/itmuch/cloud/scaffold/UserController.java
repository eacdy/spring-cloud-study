package com.itmuch.cloud.scaffold;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作用：测试服务实例的相关内容
 * @author eacdy
 */
@RestController
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private DiscoveryClient discoveryClient;

	/**
	 * 注：@GetMapping("/{id}")是spring 4.3的新注解等价于：
	 * @RequestMapping(value = "/id", method = RequestMethod.GET)
	 * 类似的注解还有@PostMapping等等
	 * @param id
	 * @return 本地服务实例的信息
	 */
	@GetMapping("/{id}")
	public ServiceInstance queryById(@PathVariable Integer id) {
		LOGGER.debug("接收的参数：{}", id);
		ServiceInstance localServiceInstance = this.discoveryClient.getLocalServiceInstance();
		return localServiceInstance;
	}
}
