package com.colorcc.ddrpc.sample.service;

import java.util.Date;

import com.colorcc.ddrpc.core.annotation.DdrpcService;

//@Service("sampleService")
@DdrpcService(ibn="sampleServiceImpl")
public interface SampleService {
	
	public String say();
	
	public String say(String name, Integer age, String desc);
	
	// Date --> long
	// Integer --> int 等需要处理，类型不匹配会报错
	public String say(String name, Integer age, Date date);
	public String say(String name, int age, Date date);

}
