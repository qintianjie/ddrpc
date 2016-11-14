package com.colorcc.ddrpc.sample.consumer.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/jack${qtj}.properties")
@ComponentScan("com.colorcc.ddrpc.sample.service, com.colorcc.ddrpc.sample.consumer.config, com.colorcc.ddrpc.sample.consumer.controller")
@SpringBootApplication
public class ConsumerMain {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerMain.class, args);
		System.out.println("started...");
	}

}
