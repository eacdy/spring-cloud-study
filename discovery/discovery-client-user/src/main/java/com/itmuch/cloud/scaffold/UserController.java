package com.itmuch.cloud.scaffold;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);
	@Autowired
	private DiscoveryClient client;

	@GetMapping("/{id}")
	public void queryById(@PathVariable Integer id) {
		ServiceInstance localServiceInstance = this.client.getLocalServiceInstance();
		this.logger.info(localServiceInstance.toString());
	}
}
