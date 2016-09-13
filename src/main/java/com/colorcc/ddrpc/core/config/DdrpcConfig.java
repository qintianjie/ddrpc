package com.colorcc.ddrpc.core.config;

import org.springframework.context.annotation.Configuration;

import com.colorcc.ddrpc.core.annotation.DdrpcScan;

@Configuration
@DdrpcScan("com.colorcc.sample.service")
public class DdrpcConfig {

}
