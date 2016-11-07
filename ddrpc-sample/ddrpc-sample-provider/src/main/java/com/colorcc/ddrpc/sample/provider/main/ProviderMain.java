package com.colorcc.ddrpc.sample.provider.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/jack${qtj}.properties")
@ComponentScan("com.colorcc.ddrpc.sample.service, com.colorcc.ddrpc.sample.provider.config")
@SpringBootApplication
public class ProviderMain {

	public static void main(String[] args) {
		SpringApplication.run(ProviderMain.class, args);
	}

}
