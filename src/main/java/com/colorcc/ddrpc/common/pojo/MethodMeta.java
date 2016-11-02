package com.colorcc.ddrpc.common.pojo;

import java.io.Serializable;

import com.colorcc.ddrpc.core.proxy.ServiceProxy;


public class MethodMeta implements Serializable {
	
	private static final long serialVersionUID = 1957003951573016924L;

	private String               methodName;

    private Class<?>[]           parameterTypes;

//    private Object[]             arguments;
//
//    private Map<String, String>  attachments;
    
    private transient ServiceProxy<?> serviceProxy;
    
    public ServiceProxy<?> getServiceProxy() {
		return serviceProxy;
	}

	public void setServiceProxy(ServiceProxy<?> serviceProxy) {
		this.serviceProxy = serviceProxy;
	}

	public MethodMeta() {
    	
    }
    
    public MethodMeta(String methodName, Class<?>[] parameterTypes, ServiceProxy<?> serviceProxy) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
        this.serviceProxy = serviceProxy;
    } 
    
//    public MethodMeta(String methodName, Class<?>[] parameterTypes, Object[] arguments, Map<String, String> attachments) {
//        this.methodName = methodName;
//        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
////        this.arguments = arguments == null ? new Object[0] : arguments;
////        this.attachments = attachments == null ? new HashMap<String, String>() : attachments;
////        this.invoker = invoker;
//    } 

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

//	public Object[] getArguments() {
//		return arguments;
//	}
//
//	public void setArguments(Object[] arguments) {
//		this.arguments = arguments;
//	}
//
//	public Map<String, String> getAttachments() {
//		return attachments;
//	}
//
//	public void setAttachments(Map<String, String> attachments) {
//		this.attachments = attachments;
//	}
    
    

//    private transient Invoker<?> invoker;

}
