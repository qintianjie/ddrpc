package com.colorcc.ddrpc.core.test.javassist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtField.Initializer;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class JavassistMain {

	public static void main(String[] args) throws CannotCompileException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException {
		
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath((JavassistMain.class)));  // set classpath
		CtClass implCc = pool.makeClass("com.colorcc.ddrpc.core.test.javassist.JavassistProxyImpl");
		
		CtField cf = CtField.make("private String name;", implCc);
		implCc.addField(cf);
		CtMethod cmGetName = CtNewMethod.make("public String getName() {return this.name;}", implCc);
		implCc.addMethod(cmGetName);
		CtMethod cmSetName = CtNewMethod.make("public void setName(String name) {this.name = name;}", implCc);
		implCc.addMethod(cmSetName);
		
		// 添加私有成员name及其getter、setter方法  
        CtField cfDesc = new CtField(pool.get("java.lang.String"), "desc", implCc);  
        cfDesc.setModifiers(Modifier.PRIVATE);  
        implCc.addMethod(CtNewMethod.setter("setDesc", cfDesc));
        implCc.addMethod(CtNewMethod.getter("getDesc", cfDesc));
        implCc.addField(cfDesc, Initializer.constant("desc default"));
        
        // 添加构造函数
        CtConstructor cons = new CtConstructor (new CtClass[] {}, implCc);
		cons.setBody("{desc = \"wakakak\";}");
		implCc.addConstructor(cons);
		
		CtMethod cmSay = CtNewMethod.make("public void say() {System.out.println(\"Hello, my name is name = \" + this.name);}", implCc);
		implCc.addMethod(cmSay);
		
		
		
		Class<?> implClass = implCc.toClass();
		System.out.println("class name: " + implClass.getName());
		Object obj = implClass.newInstance();
		for (Method method : implClass.getDeclaredMethods()) {
			System.out.println("method : " + method.getName() + ", " + Void.TYPE.equals(method.getReturnType()));
			if ("setName".equals(method.getName())) {
				method.invoke(obj, new Object[] {"Jack"});
			} else if ("say".equals(method.getName())) {
				method.invoke(obj, new Object[] {});
			}
		}
		
//		JavassistTest jt = new JavassistTest();
		
		
	}

}
