package com.colorcc.ddrpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在接口上，指定 service 对应 impl  的  beanName
 * 默认用 className + "Impl"后，第一个字母小写命名
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:45:37 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DdrpcService {
	
	String ibn() default "";

}
