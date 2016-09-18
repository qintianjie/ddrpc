package com.colorcc.ddrpc.core.config;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

//@Component
public class SampleBeanProcessor implements BeanFactoryPostProcessor, BeanPostProcessor, InstantiationAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println(" ==================> SampleBeanProcessor : postProcessBeanDefinitionRegistry");
	}


	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println(" ==================> SampleBeanProcessor : postProcessBeforeInstantiation "  + beanName);
		return null;
	}
	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
		System.out.println(" ==================> SampleBeanProcessor : postProcessMergedBeanDefinition " + beanName );
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		System.out.println(" ==================> SampleBeanProcessor : postProcessAfterInstantiation " + beanName);
		return false;
	}

	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
		System.out.println(" ==================> SampleBeanProcessor : postProcessPropertyValues "  + beanName);
		return pvs;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println(" ==================> SampleBeanProcessor : postProcessBeforeInitialization "  + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println(" ==================> SampleBeanProcessor : postProcessAfterInitialization "  + beanName);
		return bean;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println(" ==================> SampleBeanProcessor : postProcessBeanFactory ");
	}

}
