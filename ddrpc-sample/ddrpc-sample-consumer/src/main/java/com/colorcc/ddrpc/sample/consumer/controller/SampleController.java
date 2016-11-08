package com.colorcc.ddrpc.sample.consumer.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colorcc.ddrpc.sample.service.SampleService;


@RestController
@RequestMapping(value="/sample")
public class SampleController {
	
	@Autowired
	private SampleService sampleService;
	
	@GetMapping(value="/say")
	public String say(@RequestParam("name") String name) {
		
		return sampleService.say(name, 8, new Date());
		
	}

}
