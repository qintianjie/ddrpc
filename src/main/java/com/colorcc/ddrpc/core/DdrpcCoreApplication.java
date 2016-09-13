package com.colorcc.ddrpc.core;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.colorcc.sample.service.SampleService;

//@ComponentScan(basePackages="com.example.gwtspringpoc.server",
//excludeFilters=@Filter(type=FilterType.ANNOTATION,
//                       value=Controller.class))
//@Import(Object.class)
//@ImportResource("classpath:application-context-provider.xml")
//@EnableConfigurationProperties({DdrpcConfig.class}) 
//@EnableConfigurationProperties(DdrpcConfig.class)
@SpringBootApplication
public class DdrpcCoreApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdrpcCoreApplication.class, args);
//		String[] names = context.getBeanDefinitionNames();
//		for (String name: names) {
//			System.out.println("name: " + name);
//		}
		
		SampleService sampleService =(SampleService) context.getBean("sampleService");
		sampleService.say("jack", 12, new Date());
		System.out.println("done");
	}
}
