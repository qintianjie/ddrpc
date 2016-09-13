package com.colorcc.ddrpc.core.config;

import org.springframework.context.annotation.Configuration;

import com.colorcc.ddrpc.core.annotation.DdrpcScan;

@Configuration
//@ConfigurationProperties(ignoreUnknownFields = false, prefix = "ddrpc", locations = "classpath:application.properties")
@DdrpcScan("com.colorcc.sample.service")
public class DdrpcConfig {
	
//	private String basePackage;
//
//	public String getBasePackage() {
//		return basePackage;
//	}
//
//	public void setBasePackage(String basePackage) {
//		this.basePackage = basePackage;
//	}
	

}
