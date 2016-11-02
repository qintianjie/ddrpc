package com.colorcc.ddrpc.transport.netty.sample;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.common.pojo.MethodMeta;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.core.proxy.ServiceProxyClient;
import com.colorcc.ddrpc.core.proxy.filter.Filter;
import com.colorcc.ddrpc.core.proxy.filter.FilterFactory;
import com.colorcc.ddrpc.core.proxy.filter.TimeFilter;
import com.colorcc.ddrpc.sample.service.SampleService;
import com.colorcc.ddrpc.transport.netty.NettyClient;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class ClientMain {

	public static void main(String[] args) {
		// service
		Method[] methods = SampleService.class.getDeclaredMethods();
		
		for (Method method : methods){
			
			
			// netty client
			NettyClient client = new NettyClient(); 
			
			// client proxy
			ServiceProxyClient<SampleService> clientProxy = new ServiceProxyClient<>(SampleService.class, null, client);
			
			// client with filter
			Filter timeFilter = new TimeFilter();
			List<Filter> filters = new LinkedList<>();
			filters.add(timeFilter);
			ServiceProxy<SampleService> clientProxyWithFilter = FilterFactory.buildInvokerChain(clientProxy, filters);
			
			// construct request by method method
			RpcRequest request = new RpcRequest();
			request.setMethodMeta(new MethodMeta(method.getName(), method.getParameterTypes(), clientProxyWithFilter));
			request.setId(UUID.randomUUID().toString());
			request.setParamValues(new Object[] {});
			
			// method invoke
			RpcResponse response = clientProxyWithFilter.invoke(request);
			
			System.out.println(JSON.toJSONString(response));

		}
	}

}
