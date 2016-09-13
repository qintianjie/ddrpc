package com.colorcc.ddrpc.core.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.colorcc.ddrpc.core.config.DdrpcConfig;

//@Component
//@EnableConfigurationProperties(DdrpcConfig.class) 
public class TestController implements InitializingBean {
	@Autowired
	DdrpcConfig ddrpcConfig;

	@Override
	public void afterPropertiesSet() throws Exception {
//		System.out.println(ddrpcConfig.getBasePackage());
	}
	
	
	
	
}
