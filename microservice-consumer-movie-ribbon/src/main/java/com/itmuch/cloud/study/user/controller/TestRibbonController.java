package com.itmuch.cloud.study.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.itmuch.cloud.study.user.entity.User;
import com.itmuch.cloud.study.user.service.TestRibbonService;

@RestController
public class TestRibbonController {
	@Autowired
	private TestRibbonService testRibbonService;

	@GetMapping("/ribbon/{id}")
	public User findById(@PathVariable Long id) {
		return this.testRibbonService.findById(id);
	}
}
