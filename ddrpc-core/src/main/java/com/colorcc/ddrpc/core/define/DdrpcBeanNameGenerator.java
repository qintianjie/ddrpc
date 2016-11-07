package com.colorcc.ddrpc.core.define;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * Bean name generator
 * Default is xxx.class.getName()
 * 
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:34:16 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class DdrpcBeanNameGenerator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		String[] existNames = registry.getBeanDefinitionNames();
		String name = definition.getBeanClassName();
		boolean nameExist = hasName(existNames, name);
		while (nameExist) {
			name = BeanDefinitionReaderUtils.generateBeanName(definition, registry);
			nameExist = hasName(existNames, name);
		}
		return name;
	}

	private boolean hasName(String[] existNames, String name) {
		boolean hasName = false;
		for (String existName : existNames) {
			if (existName.equals(name)) {
				hasName = true;
				break;
			}
		}
		return hasName;
	}

}
