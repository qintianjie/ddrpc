package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.common.tools.URL;


public interface ProxyFactory {
	/**
	 * Provider 端，通过  impl + interface 生成 ServiceProxyServer 即  所需的 serviceProxy
	 * Consumer 端， ReferenceFactoryBean 的 afterproperties 阶段生成一个全新的 erviceProxyClient， 即所需的 serviceProxy。 
	 * 				这里有个 NettyClient， 对方法的调用，最终会通过这个 client 发请求到 provider 端
	 * @param proxy
	 * @param type
	 * @param url
	 * @return
	 * @throws Exception
	 */
	<T> ServiceProxy<T> getProxy(T proxy, Class<T> type, URL url) throws Exception;
	
	/**
	 * 有了 serviceProxy 作为 impl, 再通过  JDK or Javassist 得到一个新的 proxy 就很容易了。
	 * @param serviceProxy
	 * @return
	 * @throws Exception
	 */
	<T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception;

}
