package com.itmuch.cloud.study.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.itmuch.cloud.study.user.entity.User;

@RestController
public class TestRibbonController {
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/ribbon/{id}")
	public User findById(@PathVariable Long id) {
		// http://服务提供者的serviceId/url
		return this.restTemplate.getForObject("http://microservice-provider-user/1", User.class);
	}
}
