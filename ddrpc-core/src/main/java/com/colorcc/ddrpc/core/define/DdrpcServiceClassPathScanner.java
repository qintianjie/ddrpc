package com.colorcc.ddrpc.core.define;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.colorcc.ddrpc.core.annotation.DdrpcService;
import com.colorcc.ddrpc.core.beans.ReferenceFactoryBean;
import com.colorcc.ddrpc.core.beans.ServiceFactoryBean;

/**
 * Service class path scan for bean definition.
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:35:20 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class DdrpcServiceClassPathScanner extends ClassPathBeanDefinitionScanner {

	private boolean addToConfig = true;

	private Class<? extends Annotation> annotationClass;

	/**
	 * 每一要定义的 service class
	 */
	private Class<?> markerInterface;

	/**
	 * 每个 Service 都映射成该类型
	 */
	private ServiceFactoryBean<?> serviceFactoryBean = new ServiceFactoryBean<>();
	
	/**
	 * 每个 Client 都映射成该类型
	 */
	private ReferenceFactoryBean<?> referenceFactoryBean = new ReferenceFactoryBean<>();
	
	private boolean isProvider;
	

	public boolean isProvider() {
		return isProvider;
	}

	public void setProvider(boolean isProvider) {
		this.isProvider = isProvider;
	}

	/**
	 * 工具类，用户得到 spring container信息
	 */
	private String containerHook;
	public String getContainerHook() {
		return containerHook;
	}

	public void setContainerHook(String containerHook) {
		this.containerHook = containerHook;
	}

	public boolean isAddToConfig() {
		return addToConfig;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public Class<?> getMarkerInterface() {
		return markerInterface;
	}

	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}
	
	public ReferenceFactoryBean<?> getReferenceFactoryBean() {
		return referenceFactoryBean;
	}

	public void setReferenceFactoryBean(ReferenceFactoryBean<?> mapperFactoryBean) {
		this.referenceFactoryBean = (mapperFactoryBean != null ? mapperFactoryBean : new ReferenceFactoryBean<>());
	}

	public ServiceFactoryBean<?> getServiceFactoryBean() {
		return serviceFactoryBean;
	}

	public void setServiceFactoryBean(ServiceFactoryBean<?> mapperFactoryBean) {
		this.serviceFactoryBean = (mapperFactoryBean != null ? mapperFactoryBean : new ServiceFactoryBean<>());
	}

	public DdrpcServiceClassPathScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public void registerFilters() {
		boolean acceptAllInterfaces = true;

		// if specified, use the given annotation and / or marker interface
		if (this.annotationClass != null) {
			addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
			acceptAllInterfaces = false;
		}

		// override AssignableTypeFilter to ignore matches on the actual marker
		// interface
		if (this.markerInterface != null) {
			addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
				@Override
				protected boolean matchClassName(String className) {
					return false;
				}
			});
			acceptAllInterfaces = false;
		}

		if (acceptAllInterfaces) {
			// default include filter that accepts all classes
			addIncludeFilter(new TypeFilter() {
				@Override
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
					return true;
				}
			});
		}

		// exclude package-info.java
		addExcludeFilter(new TypeFilter() {
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
				String className = metadataReader.getClassMetadata().getClassName();
				return className.endsWith("package-info");
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		} else {
			logger.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface" + ". Bean already defined with the same name!");
			return false;
		}
	}

	/**
	 * Calls the parent search that will search and register all the candidates.
	 * Then the registered objects are post processed to set them as
	 * MapperFactoryBeans
	 */
	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

		if (beanDefinitions.isEmpty()) {
			logger.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
		} else {
			processBeanDefinitions(beanDefinitions);
		}

		return beanDefinitions;
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		GenericBeanDefinition definition;
		for (BeanDefinitionHolder holder : beanDefinitions) {
			definition = (GenericBeanDefinition) holder.getBeanDefinition();
			if (logger.isDebugEnabled()) {
				logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
			}

			// the mapper interface is the original class of the bean
			// but, the actual class of the bean is MapperFactoryBean
			definition.setAttribute("name", definition.getBeanClassName());
			definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
			if (this.isProvider()) {
				String ibn = "";
				try {
					Class<?> beanType = Class.forName(definition.getBeanClassName());
					if (beanType != null) {
						DdrpcService ddrpcAnno = beanType.getAnnotation(DdrpcService.class);
						if (ddrpcAnno != null) {
							ibn = ddrpcAnno.ibn();
						}

						if (StringUtils.isBlank(ibn)) {
							String simpleName = beanType.getSimpleName();
							Character c = simpleName.charAt(0);
							ibn = simpleName.replace(simpleName.charAt(0), Character.toLowerCase(c)) + "Impl";
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				definition.setBeanClass(this.serviceFactoryBean.getClass());
				if (StringUtils.isNotBlank(ibn)) {
					definition.getPropertyValues().add("impl", new RuntimeBeanReference(ibn));
				}
			} else {
				definition.setBeanClass(this.referenceFactoryBean.getClass());
			}
			definition.getPropertyValues().add("addToConfig", this.addToConfig);
			
			boolean explicitFactoryUsed = false;
			if (this.containerHook != null) {
				if (explicitFactoryUsed) {
					logger.warn("Cannot use both: serviceFactoryRef and sqlSessionFactory together. serviceFactoryRef is ignored.");
				}
				definition.getPropertyValues().add("containerHook", new RuntimeBeanReference(this.containerHook));
				explicitFactoryUsed = true;
			}

			if (!explicitFactoryUsed) {
				if (logger.isDebugEnabled()) {
					logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
				}
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			}
		}
	}

}
