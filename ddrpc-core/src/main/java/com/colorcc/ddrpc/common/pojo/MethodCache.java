package com.colorcc.ddrpc.common.pojo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCache {

	private static final ConcurrentHashMap<String, Method> methods = new ConcurrentHashMap<>();

//	private static final ConcurrentHashMap<String, MethodMeta> methodMetas = new ConcurrentHashMap<>();
//
//	// @TODO 需要考虑带参数类型
//	public static void registerMethodMetas(Class<?> iface, Object impl, ServiceProxyServer<?> proxy) {
//		for (Method m : impl.getClass().getMethods()) {
//			m.setAccessible(true);
//			Class<?>[] parameterTypes = m.getParameterTypes();
//			String string = Arrays.toString(parameterTypes);
//			
//			MethodMeta meta = new MethodMeta();
//			meta.setMethodName(m.getName());
//			meta.setParameterTypes(m.getParameterTypes());
//			meta.setServiceProxy(proxy);
//			methodMetas.putIfAbsent(iface.getName() + "." + m.getName() + "_" + string, meta);
//		}
//	}

	// @TODO 需要考虑带参数类型
	public static void registerMethod(Class<?> iface, Object impl) {
		for (Method m : impl.getClass().getMethods()) {
			m.setAccessible(true);
			Class<?>[] parameterTypes = m.getParameterTypes();
			String string = Arrays.toString(parameterTypes);
			methods.putIfAbsent(iface.getName() + "." + m.getName() + "_" + string, m);
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
	
//	// @TODO 需要考虑带参数类型
//		public static MethodMeta getMethodMeta(String serviceName, String methodName) {
//			return methodMetas.get("" + serviceName + "." + methodName);
//		}
//
//		// @TODO 需要考虑带参数类型
//		public static MethodMeta getMethodMeta(Class<?> iface, String methodName) {
//			return methodMetas.get("" + iface.getName() + "." + methodName);
//		}

	public static Method getMethod(Class<?> iface, MethodMeta meta) {
		Class<?>[] parameterTypes = meta.getParameterTypes();
		String string = Arrays.toString(parameterTypes);
		return methods.get(iface.getName() + "." + meta.getMethodName() + "_" + string);
	}

}
