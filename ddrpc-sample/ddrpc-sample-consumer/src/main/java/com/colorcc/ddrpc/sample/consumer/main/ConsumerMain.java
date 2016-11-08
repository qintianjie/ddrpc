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
//		ConfigurableApplicationContext context = SpringApplication.run(ConsumerMain.class, args);
//		// @formatter:off
////		for (String name : context.getBeanDefinitionNames()) {
////			System.out.println(name);
////		}
//		// @formatter:on
//		System.out.println("==========================================================");
//		SampleService sampleService = (SampleService) context.getBean(SampleService.class.getName());
////		System.out.println(sampleService.say("jack", new Integer(2), "oliaoba"));
////		System.out.println(sampleService.say("jack", 202, "oliaoba"));
//		System.out.println(sampleService.say("jack", 229, new Date()));
////		for (int i = 2; i < 100; i++) {
////			System.out.println(sampleService.say("jack_" + i, new Integer(i), "oliaoba_" + i));
////		}
	}

}
