package com.colorcc.sample.service.impl;

import org.springframework.stereotype.Component;

import com.colorcc.sample.service.HelloService;

@Component
public class HelloServiceImpl implements HelloService {

	@Override
	public String hello() {
		return "[Hello Service] hello";
	}

	@Override
	public String hello(String name) {
		return "[Hello Service] hello " + name;
	}

}
