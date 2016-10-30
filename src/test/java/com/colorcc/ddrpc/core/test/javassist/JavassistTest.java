package com.colorcc.ddrpc.core.test.javassist;

public class JavassistTest {
	
	public void test() {
		System.out.println(this.getClass().getName() + " : test. ");
	}
	
	public Object invoke() {
		System.out.println(this.getClass().getName() + " : invoke. ");
		return Void.class;
	}

}
