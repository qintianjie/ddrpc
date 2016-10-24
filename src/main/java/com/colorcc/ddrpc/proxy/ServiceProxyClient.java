package com.colorcc.ddrpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.colorcc.ddrpc.pojo.MethodMeta;
import com.colorcc.ddrpc.tools.URL;
import com.colorcc.ddrpc.transport.netty.NettyClient;
import com.colorcc.ddrpc.transport.netty.callback.ClientCallback;
import com.colorcc.ddrpc.transport.netty.callback.ClientCallbackImpl;
import com.colorcc.ddrpc.transport.netty.handler.RpcClientInHandler;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class ServiceProxyClient<T> extends AbstractServiceProxy<T> {

	private NettyClient client;

	public NettyClient getClient() {
		return client;
	}
	
	public ServiceProxyClient(Class<T> ifs, URL url) {
		super(ifs, url);
	}

	public T getProxy() {
		Class<?>[] infs = { getInterface() };
		@SuppressWarnings("unchecked")
		T proxy = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), infs, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				MethodMeta methodMeta = new MethodMeta(method.getName(), method.getParameterTypes());
				
				ClientCallback<RpcResponse> callback = new ClientCallbackImpl<>();
				RpcRequest request = new RpcRequest(methodMeta, args, null);
				RpcClientInHandler<RpcResponse> handler = new RpcClientInHandler<>(request, callback);
				NettyClient client = new NettyClient();
				client.init();
				getClient().request(request);
				Object result = callback.getResult();
				return result;
			}
		});

		return proxy;
	}

}
