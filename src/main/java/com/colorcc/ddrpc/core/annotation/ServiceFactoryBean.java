package com.colorcc.ddrpc.core.annotation;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 每个Service对应的FactoryBean
 * 
 * @author Duoduo
 *
 * @param <T>
 */
public class ServiceFactoryBean<T> implements FactoryBean<T>, InitializingBean, BeanNameAware {

	private Class<T> mapperInterface;
	private boolean addToConfig = true;
	private transient String beanName;
	ServiceReposity reposity = new ServiceReposity();

	private DdrpcFactoryBean ddrpcFactoryBean;

	public void setDdrpcFactoryBean(DdrpcFactoryBean ddrpcFactoryBean) {
		this.ddrpcFactoryBean = ddrpcFactoryBean;
	}
	

	public DdrpcFactoryBean getDdrpcFactoryBean() {
		return ddrpcFactoryBean;
	}


	public ServiceFactoryBean(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public ServiceFactoryBean() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!reposity.hasMapper(this.mapperInterface)) {
			reposity.addMapper(this.mapperInterface, ddrpcFactoryBean);
		}
	}

	@Override
	public T getObject() throws Exception {
		return reposity.getMapper(this.mapperInterface, ddrpcFactoryBean);
	}

	@Override
	public Class<?> getObjectType() {
		return this.mapperInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public boolean isAddToConfig() {
		return addToConfig;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}


	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

}
