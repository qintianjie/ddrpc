package com.colorcc.sample.service.impl;

import java.text.DateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.colorcc.sample.service.SampleService;
@Component
public class SampleServiceImpl implements SampleService, InitializingBean {
	
	@PostConstruct
    public void postConst() {
        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>> postConst ");
    }

    @PreDestroy
    public void preDestroy() {
    	System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>> preDestroy ");
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>> afterPropertiesSet ");
	}

	@Override
	public String say() {
		return "[sample say]sample say.";
	}

	@Override
	public String say(String name, int age, Date date) {
		String str = "[sample say] name: " + name + ", age: " + age + ", date: " + DateFormat.getInstance().format(date);
		return str;
	}

}
