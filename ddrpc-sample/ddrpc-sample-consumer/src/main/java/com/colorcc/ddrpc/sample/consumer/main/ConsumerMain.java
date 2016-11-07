package com.colorcc.ddrpc.sample.consumer.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.colorcc.ddrpc.sample.service.SampleService;

@PropertySource("classpath:/jack${qtj}.properties")
@ComponentScan("com.colorcc.ddrpc.sample.service, com.colorcc.ddrpc.sample.consumer.config")
@SpringBootApplication
public class ConsumerMain {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ConsumerMain.class, args);
		// @formatter:off
//		for (String name : context.getBeanDefinitionNames()) {
//			System.out.println(name);
//		}
		// @formatter:on
		System.out.println("==========================================================");
		SampleService sampleService = (SampleService) context.getBean(SampleService.class.getName());
		System.out.println(sampleService.say("jack", new Integer(12), "oliaoba"));
	}

}
