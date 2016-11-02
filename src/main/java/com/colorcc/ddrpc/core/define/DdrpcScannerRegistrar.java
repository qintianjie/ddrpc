package com.colorcc.ddrpc.core.define;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.colorcc.ddrpc.core.annotation.DdrpcScan;
import com.colorcc.ddrpc.core.beans.DdrpcFactoryBean;
import com.colorcc.ddrpc.core.beans.ReferenceFactoryBean;
import com.colorcc.ddrpc.core.beans.ServiceFactoryBean;

/**
 * 通过实现 ImportBeanDefinitionRegistrar 接口达到对 bean definition 自定义的目的
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:39:56 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class DdrpcScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
	private ResourceLoader resourceLoader;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DdrpcScan.class.getName()));
		DdrpcServiceClassPathScanner scanner = new DdrpcServiceClassPathScanner(registry);
		// this check is needed in Spring 3.1
		if (resourceLoader != null) {
			scanner.setResourceLoader(resourceLoader);
		}

		Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
		if (!Annotation.class.equals(annotationClass)) {
			scanner.setAnnotationClass(annotationClass);
		}

		Class<?> markerInterface = annoAttrs.getClass("markerInterface");
		if (!Class.class.equals(markerInterface)) {
			scanner.setMarkerInterface(markerInterface);
		}
//		Class<? extends ServiceFactoryBean<?>> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
		Class<? extends DdrpcFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
		boolean isProvider = annoAttrs.getBoolean("isProvider");
		scanner.setProvider(isProvider);
		
		if (!ServiceFactoryBean.class.equals(mapperFactoryBeanClass) && isProvider) {
			scanner.setServiceFactoryBean((ServiceFactoryBean<?>)BeanUtils.instantiateClass(mapperFactoryBeanClass));
		} else if(!ReferenceFactoryBean.class.equals(mapperFactoryBeanClass)&& !isProvider) {
			scanner.setReferenceFactoryBean((ReferenceFactoryBean<?>)BeanUtils.instantiateClass(mapperFactoryBeanClass));
		}
		
		Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
	    if (!BeanNameGenerator.class.equals(generatorClass)) {
	      scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
	    }
		
		scanner.setContainerHook(annoAttrs.getString("containerHook"));

		List<String> basePackages = getBasePackages(annoAttrs);

		scanner.registerFilters();
		scanner.doScan(StringUtils.toStringArray(basePackages));

	}

	private List<String> getBasePackages(AnnotationAttributes annoAttrs) {
		List<String> basePackages = new ArrayList<String>();
		for (String pkg : annoAttrs.getStringArray("value")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		for (String pkg : annoAttrs.getStringArray("basePackages")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
			basePackages.add(ClassUtils.getPackageName(clazz));
		}
		return basePackages;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
