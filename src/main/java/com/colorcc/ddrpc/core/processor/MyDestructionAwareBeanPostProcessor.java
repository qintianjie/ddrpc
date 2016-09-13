package com.colorcc.ddrpc.core.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
//@Component
public class MyDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("MyDestructionAwareBeanPostProcessor ... postProcessBeforeInitialization : " + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("MyDestructionAwareBeanPostProcessor ... postProcessAfterInitialization : " + beanName);
		return bean;
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		System.out.println("MyDestructionAwareBeanPostProcessor ... postProcessBeforeDestruction : " + beanName);

	}

	@Override
	public boolean requiresDestruction(Object bean) {
		System.out.println("MyDestructionAwareBeanPostProcessor ... requiresDestruction : ");
		return false;
	}

}
