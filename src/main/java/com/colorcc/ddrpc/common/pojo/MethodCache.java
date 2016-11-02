package com.colorcc.ddrpc.common.pojo;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCache {

	private static final ConcurrentHashMap<String, Method> methods = new ConcurrentHashMap<>();
	
	// @TODO 需要考虑带参数类型
	public static void registerMethod(Class<?> iface, Object impl) {
		for (Method m : impl.getClass().getMethods()) {
			m.setAccessible(true);
//			Class<?>[] parameterTypes = m.getParameterTypes();
//			String string = Arrays.toString(parameterTypes);
			methods.putIfAbsent(iface.getName() + "." + m.getName(), m);
		}
	}

	// @TODO 需要考虑带参数类型
	public static Method getMethod(String serviceName, String methodName) {
		return methods.get("" + serviceName + "." + methodName);
	}

	// @TODO 需要考虑带参数类型
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
