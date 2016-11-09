package com.colorcc.ddrpc.core.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import com.colorcc.ddrpc.common.tools.URL;

public class ZkServiceRegister {

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
		String provider = url.getHost() + ":" + url.getPort();
		String path = "/provider/" + serviceName + "/" + provider;
		CuratorFramework zkClient = ZkServiceRegister.getZkClient();
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
	
	

}
