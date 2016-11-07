package com.colorcc.ddrpc.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import com.colorcc.ddrpc.core.beans.DdrpcFactoryBean;
import com.colorcc.ddrpc.core.beans.ServiceFactoryBean;
import com.colorcc.ddrpc.core.define.DdrpcBeanNameGenerator;
import com.colorcc.ddrpc.core.define.DdrpcScannerRegistrar;

/**
 * 扩展 annotaion
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:41:14 PM 
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DdrpcScannerRegistrar.class)
public @interface DdrpcScan {
	/**
	 * packages will be scan
	 * 
	 * @return
	 */
	String[] value() default {};

	/**
	 * 功能同 value
	 * 
	 * @return
	 */
	String[] basePackages() default {};

	/**
	 * 功能同 value 
	 * Type-safe alternative to {@link #basePackages()} for specifying
	 * the packages to scan for annotated components. The package of each class
	 * specified will be scanned.
	 * <p>
	 * Consider creating a special no-op marker class or interface in each
	 * package that serves no purpose other than being referenced by this
	 * attribute.
	 */
	Class<?>[] basePackageClasses() default {};

	/**
	 * This property specifies the annotation that the scanner will search for.
	 * <p>
	 * The scanner will register all interfaces in the base package that also
	 * have the specified annotation.
	 * <p>
	 * Note this can be combined with markerInterface.
	 */
	Class<? extends Annotation> annotationClass() default Annotation.class;

	/**
	 * Service 类型 
	 * This property specifies the parent that the scanner will search for.
	 * <p>
	 * The scanner will register all interfaces in the base package that also
	 * have the specified interface class as a parent.
	 * <p>
	 * Note this can be combined with annotationClass.
	 */
	Class<?> markerInterface() default Class.class;

	/**
	 * 每个 markerInterface 都生成该类型的 proxy 
	 * Specifies a custom ServiceFactoryBean to return a ddrpc proxy as spring bean.
	 */
	Class<? extends DdrpcFactoryBean> factoryBean() default ServiceFactoryBean.class;

	/**
	 * 命名规则
	 * The {@link BeanNameGenerator} class to be used for naming detected
	 * components within the Spring container.
	 */
	Class<? extends BeanNameGenerator> nameGenerator() default DdrpcBeanNameGenerator.class;

	/**
	 * 该类主要实现了 ApplicationContextAware 等接口，可实时拿到 context
	 * @return
	 */
	String containerHook() default "containerHook";
	
	boolean isProvider() default true;

}
