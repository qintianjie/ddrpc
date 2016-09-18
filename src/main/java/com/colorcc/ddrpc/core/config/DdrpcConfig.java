package com.colorcc.ddrpc.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.colorcc.ddrpc.core.annotation.DdrpcFactoryBean;
import com.colorcc.ddrpc.core.annotation.DdrpcScan;

@Configuration
@ConditionalOnProperty(prefix = "ddrpc", name = "enabled", havingValue = "true", matchIfMissing = true)
@DdrpcScan("com.colorcc.sample.service")
public class DdrpcConfig {
	
	@Bean
	public DdrpcFactoryBean ddrpcFactoryBean() {
		return new DdrpcFactoryBean();
	}

}
