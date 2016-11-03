package com.colorcc.ddrpc.sample.config.consumer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.colorcc.ddrpc.core.annotation.DdrpcScan;
import com.colorcc.ddrpc.core.beans.ContainerHook;
import com.colorcc.ddrpc.core.beans.ReferenceFactoryBean;

@Configuration
@ConditionalOnProperty(prefix = "ddrpc", name = "enabled", havingValue = "true", matchIfMissing = true)
@DdrpcScan(value = "com.colorcc.ddrpc.sample.service", isProvider=false, factoryBean=ReferenceFactoryBean.class)
public class ConsumerDdrpcConfig {
	
	@Bean
	public ContainerHook containerHook() {
		return new ContainerHook();
	}

}