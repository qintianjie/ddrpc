package com.colorcc.ddrpc;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.colorcc.ddrpc.sample.service.HelloService;
import com.colorcc.ddrpc.sample.service.SampleService;

@PropertySource("classpath:/jack${qtj}.properties")
@ComponentScan("com.colorcc.ddrpc.sample.service")
@ComponentScan("com.colorcc.ddrpc.sample.config")
@SpringBootApplication
public class ColorccDdrpcApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ColorccDdrpcApplication.class, args);
		// @formatter:off
//		for (String name : context.getBeanDefinitionNames()) {
//			System.out.println(name);
//		}
		// @formatter:on
		System.out.println("==========================================================");
		SampleService sampleService = (SampleService) context.getBean(SampleService.class.getName());
		System.out.println(sampleService.say("jack", 12, new Date()));
		System.out.println(sampleService.say());
		sampleService.say();
		System.out.println("==========================================================");
		HelloService helloService = (HelloService) context.getBean(HelloService.class.getName());
		System.out.println(helloService.hello());
		System.out.println(helloService.hello("jack"));
		System.out.println("==========================================================");

		System.out.println("done");
	}
}