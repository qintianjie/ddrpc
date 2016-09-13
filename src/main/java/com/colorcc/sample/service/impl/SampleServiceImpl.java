package com.colorcc.sample.service.impl;

import java.text.DateFormat;
import java.util.Date;

import com.colorcc.sample.service.SampleService;

public class SampleServiceImpl implements SampleService {

	@Override
	public String say() {
		return null;
	}

	@Override
	public String say(String name, int age, Date date) {
		String str = "name: " + name + ", age: " + age + ", date: " + DateFormat.getInstance().format(date);
		System.out.println(str);
		return str;
	}

}
