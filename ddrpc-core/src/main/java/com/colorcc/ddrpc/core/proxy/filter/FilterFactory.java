package com.colorcc.ddrpc.core.proxy.filter;

import java.util.List;

import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class FilterFactory {

	public static <T> ServiceProxy<T> buildInvokerChain(final ServiceProxy<T> serviceProxy, List<Filter> filters) {
		ServiceProxy<T> last = serviceProxy;
		for (int i = filters.size() - 1; i >= 0; i--) {
			final Filter filter = filters.get(i);
			final ServiceProxy<T> next = last;
			
			last = new ServiceProxy<T>() {

				@Override
				public Class<T> getInterface() {
					return serviceProxy.getInterface();
				}

				@Override
				public RpcResponse invoke(RpcRequest request) throws InterruptedException {
					return filter.invoke(next, request);
				}
				
			};
			
		}
		
		return last;
	}
}
