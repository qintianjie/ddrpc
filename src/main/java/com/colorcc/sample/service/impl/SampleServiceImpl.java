package com.colorcc.sample.service.impl;

import java.text.DateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.colorcc.sample.service.SampleService;
@Component
public class SampleServiceImpl implements SampleService {

	@Override
	public String say() {
		return "[sample say] sample say.";
	}

	@Override
	public String say(String name, int age, Date date) {
		String str = "[sample say] name: " + name + ", age: " + age + ", date: " + DateFormat.getInstance().format(date);
		return str;
	}

}
