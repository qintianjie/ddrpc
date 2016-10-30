package com.colorcc.ddrpc.core.test.javassist;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JavassistInvocationHandler implements InvocationHandler {
	
	private Object impl;
	
	public JavassistInvocationHandler (Object impl) {
		this.impl = impl;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("JavassistInvocationHandler ... " + method.getName());
		return method.invoke(impl, args);
	}

}
