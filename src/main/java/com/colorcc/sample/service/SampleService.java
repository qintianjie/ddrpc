package com.colorcc.sample.service;

import java.util.Date;

import com.colorcc.ddrpc.core.annotation.DdrpcService;

//@Service("sampleService")
@DdrpcService(ibn="sampleServiceImpl")
public interface SampleService {
	
	public String say();
	
	public String say(String name, int age, Date date);

}
