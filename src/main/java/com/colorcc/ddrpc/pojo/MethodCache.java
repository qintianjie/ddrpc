package com.colorcc.ddrpc.pojo;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCache {

	private static final ConcurrentHashMap<String, Method> methods = new ConcurrentHashMap<>();

	public static void registerMethod(Class<?> iface, Object impl) {
		for (Method m : impl.getClass().getMethods()) {
			m.setAccessible(true);
			methods.putIfAbsent(iface.getName() + "." + m.getName(), m);
		}
	}

	public static Method getMethod(String serviceName, String methodName) {
		return methods.get("" + serviceName + "." + methodName);
	}

	public static Method getMethod(Class<?> iface, String methodName) {
		return methods.get("" + iface.getName() + "." + methodName);
	}
	
//	private static final ConcurrentHashMap<String, MethodMeta> methods = new ConcurrentHashMap<>();
//
//	private static MethodMeta getMethodMeta(Method method) {
//		MethodMeta meta = new MethodMeta(method.getName(), method.getParameterTypes());
//		return meta;
//	}
//
//	public static void registerMethodMeta(Class<?> iface, Object impl) {
//		for (Method m : impl.getClass().getMethods()) {
//			MethodMeta methodMeta = getMethodMeta(m);
//			methods.putIfAbsent(iface.getName() + "." + methodMeta.getMethodName(), methodMeta);
//		}
//	}
//
//	public static MethodMeta getMethod(String serviceName, String methodName) {
//		return methods.get("" + serviceName + "." + methodName);
//	}
//
//	public static MethodMeta getMethod(Class<?> iface, String methodName) {
//		return methods.get("" + iface.getName() + "." + methodName);
//	}

}
