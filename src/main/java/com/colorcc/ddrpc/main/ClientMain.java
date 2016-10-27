package com.colorcc.ddrpc.main;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.filter.Filter;
import com.colorcc.ddrpc.filter.FilterFactory;
import com.colorcc.ddrpc.filter.TimeFilter;
import com.colorcc.ddrpc.pojo.MethodMeta;
import com.colorcc.ddrpc.proxy.ServiceProxy;
import com.colorcc.ddrpc.proxy.ServiceProxyClient;
import com.colorcc.ddrpc.transport.netty.NettyClient;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;
import com.colorcc.sample.service.SampleService;

public class ClientMain {

	public static void main(String[] args) {
		// service
		Method[] methods = SampleService.class.getDeclaredMethods();
		
		for (Method method : methods){
			// construct request by method method
			RpcRequest request = new RpcRequest();
			request.setMethodMeta(new MethodMeta(method.getName(), method.getParameterTypes()));
			request.setId(UUID.randomUUID().toString());
			request.setParamValues(new Object[] {});
			
			// netty client
			NettyClient client = new NettyClient(); 
			
			// client proxy
			ServiceProxyClient<SampleService> clientProxy = new ServiceProxyClient<>(SampleService.class, null, client);
			
			// client with filter
			Filter timeFilter = new TimeFilter();
			List<Filter> filters = new LinkedList<>();
			filters.add(timeFilter);
			ServiceProxy<SampleService> clientProxyWithFilter = FilterFactory.buildInvokerChain(clientProxy, filters);
			
			// method invoke
			RpcResponse response = clientProxyWithFilter.invoke(request);
			
			System.out.println(JSON.toJSONString(response));

		}
	}

}
