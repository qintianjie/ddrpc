package com.colorcc.ddrpc.core.test.javassist.reflect;

import javassist.tools.reflect.Metaobject;

public class VerboseMetaobj extends Metaobject {

	private static final long serialVersionUID = -4592567457750408945L;

	public VerboseMetaobj(Object self, Object[] args) {
		super(self, args);
		System.out.println("** constructed: " + self.getClass().getName());
	}

	public Object trapFieldRead(String name) {
		System.out.println("** field read: " + name);
		return super.trapFieldRead(name);
	}

	public void trapFieldWrite(String name, Object value) {
		System.out.println("** field write: " + name);
		super.trapFieldWrite(name, value);
	}

	public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
		System.out.println("** trap: " + getMethodName(identifier) + "() in " + getClassMetaobject().getName());
		return super.trapMethodcall(identifier, args);
	}
}
