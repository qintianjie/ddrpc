package com.colorcc.ddrpc.core;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.colorcc.sample.service.HelloService;
import com.colorcc.sample.service.SampleService;

@PropertySource("classpath:/jack${qtj}.properties")
@ComponentScan("com.colorcc.sample.service")
@ComponentScan("com.colorcc.ddrpc.core.config")
@ComponentScan("com.colorcc.ddrpc.core.annotation")
@SpringBootApplication
// @Import(JackConfig.class)
public class DdrpcCoreApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdrpcCoreApplication.class, args);
		for (String name : context.getBeanDefinitionNames()) {
			System.out.println(name);
		}
		// @formatter:off
		System.out.println("==========================================================");
		// SampleService sampleService =(SampleService) context.getBean("sampleService");
		SampleService sampleService = (SampleService) context.getBean(SampleService.class.getName());
		System.out.println(sampleService.say("jack", 12, new Date()));
		System.out.println(sampleService.say());
		sampleService.say();
		System.out.println("==========================================================");
		// HelloService helloService =(HelloService) context.getBean("helloService");
		// @formatter:on
		HelloService helloService = (HelloService) context.getBean(HelloService.class.getName());
		System.out.println(helloService.hello());
		System.out.println(helloService.hello("jack"));

		System.out.println("done");
	}
}
