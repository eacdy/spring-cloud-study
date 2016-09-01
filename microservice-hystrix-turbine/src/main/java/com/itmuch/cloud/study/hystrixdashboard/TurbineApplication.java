package com.itmuch.cloud.study.hystrixdashboard;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableTurbine
public class TurbineApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(TurbineApplication.class).web(true).run(args);
	}
}
