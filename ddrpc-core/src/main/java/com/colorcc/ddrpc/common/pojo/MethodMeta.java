package com.colorcc.ddrpc.common.pojo;

import java.io.Serializable;
import java.lang.reflect.Method;


public class MethodMeta implements Serializable {
	
	private static final long serialVersionUID = 1957003951573016924L;

	private String               methodName;

    private Class<?>[]           parameterTypes; 

	public MethodMeta() {
    	
    }
	
	public MethodMeta(Method m) {
        this.methodName = m.getName();
        this.parameterTypes = m.getParameterTypes();
    } 
    
    public MethodMeta(String methodName, Class<?>[] parameterTypes) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
    } 
    
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

}
