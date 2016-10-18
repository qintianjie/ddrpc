package com.colorcc.ddrpc.core.test.netty.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MethodMeta implements Serializable {
	
	private static final long serialVersionUID = 1957003951573016924L;

	private String               methodName;

    private Class<?>[]           parameterTypes;

    private Object[]             arguments;

    private Map<String, String>  attachments;
    
    
    public MethodMeta(String methodName, Class<?>[] parameterTypes, Object[] arguments, Map<String, String> attachments) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
        this.arguments = arguments == null ? new Object[0] : arguments;
        this.attachments = attachments == null ? new HashMap<String, String>() : attachments;
//        this.invoker = invoker;
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

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}
    
    

//    private transient Invoker<?> invoker;

}
