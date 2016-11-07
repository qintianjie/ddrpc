package com.colorcc.ddrpc.core.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Spring container 工具类，用于得到  container 运行时的一些属性与方法。
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 11:03:56 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class ContainerHook  implements InitializingBean, ApplicationContextAware, ApplicationListener<ApplicationEvent> {
	private ApplicationContext applicationContext;
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	   * {@inheritDoc}
	   */
	  @Override
	  public void onApplicationEvent(ApplicationEvent event) {
		  
	  }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
