package com.colorcc.ddrpc.core.test.javassist.reflect;

import javassist.tools.reflect.Loader;



public class MainDemo {
	public static void main(String[] args) throws Throwable {
        Loader cl = (Loader)MainDemo.class.getClassLoader();
        
        cl.makeReflective("sample.reflect.Person",
                          "sample.reflect.VerboseMetaobj",
                          "javassist.tools.reflect.ClassMetaobject");

        cl.run("sample.reflect.Person", args);
    }
}
