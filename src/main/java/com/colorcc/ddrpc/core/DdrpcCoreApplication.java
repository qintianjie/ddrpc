package com.colorcc.ddrpc.core;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.colorcc.sample.service.SampleService;

@SpringBootApplication
public class DdrpcCoreApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdrpcCoreApplication.class, args);
		SampleService sampleService =(SampleService) context.getBean("sampleService");
		sampleService.say("jack", 12, new Date());
		System.out.println("done");
	}
}
