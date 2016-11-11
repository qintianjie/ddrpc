package com.colorcc.ddrpc.core.zk.curator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.colorcc.ddrpc.common.tools.URL;

public class ZkUtils {

	public static CuratorFramework getZkClient() {
		// 服务注册到 ZK 节点
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181/ddrpc", retryPolicy);
		client.start();
		if (!client.getState().equals(CuratorFrameworkState.STARTED)) {
			client.start();
		}
		return client;
	}
	
	public static boolean registerProvider(URL url) {
		String serviceName = url.getParameter("service");
		String hostIpPort = url.getHost() + ":" + url.getPort();
		String path = "/" + serviceName + "/provider/" +   hostIpPort;
		CuratorFramework zkClient = ZkUtils.getZkClient();
		try {
			zkClient.delete().deletingChildrenIfNeeded().forPath(path);
		} catch (Exception e) {
		}
		try {
			zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, url.toString().getBytes());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Map<String, String> getNodeChildren(URL url) {
		String serviceName = url.getParameter("service");
		String hostIpPort = url.getHost() + ":" + url.getPort();
		String path = serviceName + "/provider/" +  "/" + hostIpPort;
		CuratorFramework zkClient = ZkUtils.getZkClient();
		Map<String, String> data = new HashMap<>();
		try {
			Stat stat = zkClient.checkExists().forPath(path);
			if (stat != null) {
				List<String> childrenName = zkClient.getChildren().forPath(path);
				if (childrenName != null) {
					for (String name : childrenName) {
						byte[] byteData = zkClient.getData().forPath(path + "/" + name);
						data.put(name, String.valueOf(byteData));
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	public static Map<String, String> getNodeChildren(String serviceProvider) {
//		String serviceName = url.getParameter("service");
//		String hostIpPort = url.getHost() + ":" + url.getPort();
//		String path = serviceName + "/provider/" +  "/" + hostIpPort;
		CuratorFramework zkClient = ZkUtils.getZkClient();
		Map<String, String> data = new HashMap<>();
		try {
			Stat stat = zkClient.checkExists().forPath(serviceProvider);
			if (stat != null) {
				List<String> childrenName = zkClient.getChildren().forPath(serviceProvider);
				if (childrenName != null) {
					for (String name : childrenName) {
						byte[] byteData = zkClient.getData().forPath(serviceProvider + "/" + name);
						data.put(name, new String(byteData));
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	

}
