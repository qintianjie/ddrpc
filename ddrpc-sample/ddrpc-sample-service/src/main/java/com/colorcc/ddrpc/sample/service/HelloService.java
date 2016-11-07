package com.colorcc.ddrpc.sample.service;


/**
 * 如果 位定义，则默认用  class.getSimpleName + "Impl" 后，首字母小写得到对应的 impl beanName
 * @author Duoduo
 *
 */
//@Service("helloService")
//@DdrpcService(ibn="helloServiceImpl")
public interface HelloService {
	
	public String hello();
	
	public String hello(String name);

}
