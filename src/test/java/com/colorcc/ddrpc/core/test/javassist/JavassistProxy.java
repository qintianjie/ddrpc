package com.colorcc.ddrpc.core.test.javassist;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicLong;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import com.colorcc.sample.service.SampleService;
import com.colorcc.sample.service.impl.SampleServiceImpl;


public  class JavassistProxy {
	
	private static final AtomicLong PROXY_CLASS_COUNTER = new AtomicLong(0);
	
	public static void main(String[] args) {
		JavassistProxy proxy = new JavassistProxy();
		SampleService sample = new SampleServiceImpl();
		JavassistInvocationHandler handler = new JavassistInvocationHandler(sample);
//		proxy.getProxy(infs)
		
		
	}
	
	
	public JavassistProxy getProxy(Class<?> infs, Object impl) throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ClassPool pool = ClassPool.getDefault();
		String packageName = infs.getClass().getPackage().getName();
		
		// =================================================================
		long id = PROXY_CLASS_COUNTER.getAndIncrement();
		String proxyImplName = packageName + ".JavassistProxy" + id;
		CtClass cc = pool.makeClass(proxyImplName);
		
		// methods & get/set method
		CtField cfMethods = CtField.make("private java.lang.reflect.Method[] methods;", cc);
		cc.addMethod(CtNewMethod.setter("setMethods", cfMethods));
		cc.addMethod(CtNewMethod.getter("getMethods", cfMethods));
		cc.addField(cfMethods);
		
		// handler & get method
		CtField cfInvocationHandler = CtField.make("private " + InvocationHandler.class.getName() + " handler;", cc);
		cc.addMethod(CtNewMethod.getter("getHandler", cfInvocationHandler));
		cc.addField(cfInvocationHandler);
		
		// 添加构造函数 & set handler
        CtConstructor cons = new CtConstructor (new CtClass[] {pool.get(InvocationHandler.class.getName())}, cc);
        cons.setModifiers(Modifier.PUBLIC);
		cons.setBody("{handler = $1;}"); // $0代码的是this，$1代表方法参数的第一个参数、$2代表方法参数的第二个参数
		cc.addConstructor(cons);

		// a method to process interface method
		for (Method method : infs.getMethods()) {
			
		}
		
//		CtMethod cmSay = CtNewMethod.make("public void say() {System.out.println(\"Hello, my name is name = \" + this.name);}", cc);
//		cc.addMethod(cmSay);
		
		Class<?> implClass = cc.toClass();
		System.out.println("class name: " + implClass.getName());
		Object obj = implClass.newInstance(); 
		
		
		return obj;
	}
	
	
	
//	private static final Map<ClassLoader, Map<String, Object>> ProxyClassloadCacheMap = new WeakHashMap<ClassLoader, Map<String, Object>>();
//	private static final Object PendingGenerationMarker = new Object();
//
//	public static void main(String[] args) {
//		JavassistProxy.getProxy(SampleService.class);
//		System.out.println("done");
//	}
//
//	/**
//	 * Get proxy.
//	 * 
//	 * @param ics
//	 * @return
//	 */
//	public static JavassistProxy getProxy(Class<?>... ics) {
//		return getProxy(getClassLoader(JavassistProxy.class), ics);
//	}
//
//	public static JavassistProxy getProxy(ClassLoader cl, Class<?>... ics) {
//		if (ics.length > 65535) {
//			throw new IllegalArgumentException("interface limit exceeded");
//		}
//
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < ics.length; i++) {
//			String itf = ics[i].getName();
//			if (!ics[i].isInterface())
//				throw new RuntimeException(itf + " is not a interface.");
//
//			Class<?> tmp = null;
//			try {
//				tmp = Class.forName(itf, false, cl);
//			} catch (ClassNotFoundException e) {
//			}
//
//			if (tmp != ics[i])
//				throw new IllegalArgumentException(ics[i] + " is not visible from class loader");
//
//			sb.append(itf).append(';');
//		}
//
//		System.out.println("sb: " + sb.toString());
//		// use interface class name list as key.
//		String key = sb.toString();
//
//		// get cache by class loader.
//		Map<String, Object> cache;
//		synchronized (ProxyClassloadCacheMap) {
//			cache = ProxyClassloadCacheMap.get(cl);
//			if (cache == null) {
//				cache = new HashMap<String, Object>();
//				ProxyClassloadCacheMap.put(cl, cache);
//			}
//		}
//
//		JavassistProxy proxy = null;
//		synchronized (cache) {
//			do {
//				Object value = cache.get(key);
//				if (value instanceof Reference<?>) {
//					proxy = (JavassistProxy) ((Reference<?>) value).get();
//					if (proxy != null)
//						return proxy;
//				}
//
//				if (value == PendingGenerationMarker) {
//					try {
//						cache.wait();
//					} catch (InterruptedException e) {
//					}
//				} else {
//					cache.put(key, PendingGenerationMarker);
//					break;
//				}
//			} while (true);
//		}
//
//		return proxy;
//	}
//
//	private static ClassLoader getClassLoader(Class<?> cls) {
//		ClassLoader cl = null;
//		try {
//			cl = Thread.currentThread().getContextClassLoader();
//		} catch (Throwable ex) {
//			// Cannot access thread context ClassLoader - falling back to system
//			// class loader...
//		}
//		if (cl == null) {
//			// No thread context class loader -> use class loader of this class.
//			cl = cls.getClassLoader();
//		}
//		return cl;
//	}

}
