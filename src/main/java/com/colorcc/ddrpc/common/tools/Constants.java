/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.colorcc.ddrpc.common.tools;

import java.util.regex.Pattern;

/**
 * 
 * Constants
 * 
 */
public class Constants {
    private Constants() {
    }

    /**
     * 全局采用逗号分隔
     */
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

    /**** 服务描述和引用 相关参数 ****/
    /**
     * URI里指定默认值,比如有key,那么DEFAULT_KEY_PREFIX+key指定的值就是该key的默认值
     */
    public static final String DEFAULT_KEY_PREFIX = "default.";

    /**
     * 服务分组key
     */
    public static final String GROUP_KEY = "group";

    /**
     * URI的路径key
     */
    public static final String PATH_KEY = "path";

    /**
     * 服务接口key
     */
    public static final String INTERFACE_KEY = "interface";

    /**
     * 服务版本key
     */
    public static final String VERSION_KEY = "version";

    /**
     * 注册中心key
     */
    public static final String REGISTRY_KEY = "registry";
    /**
     * rpc调用的超时时间key
     */
    public static final String CALL_TIMEOUT_KEY = "call.timeout";

    /**
     * 默认rpc调用超时5分钟
     */
    public static final long DEFAULT_CALL_TIMEOUT = 5 * 60 * 1000;

    /**
     * 配置多个host时使用的从节点key,内容为逗号分隔,可配多个从节点
     */
    public static final String BACKUP_KEY = "backup";

    /**
     * 连接超时key
     */
    public static final String CONNECT_TIMEOUT_KEY = "connect.timeout";

    /**
     * 默认连接超时5秒,单位毫秒,时间相关参数全局默认单位为毫秒
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 5 * 1000;

    /**
     * 会话超时key
     */
    public static final String SESSION_TIMEOUT_KEY = "session.timeout";

    /**
     * 默认会话超时1分钟,单位毫秒
     */
    public static final int DEFAULT_SESSION_TIMEOUT = 60 * 1000;

    /**** RPC相关参数 ****/
    /**
     * Remoting实现方式key
     */
    public static final String REMOTING_KEY = "remoting";

    /**
     * Remoting默认实现为netty
     */
    public static final String DEFAULT_REMOTING = "netty";

    /**
     * 标示框架连续调用时sessionid的key
     */
    public static final String SESSION_ID_KEY = "stargate.sid";

    /**
     * 标示框架单次调用时requestid的key
     */
    public static final String REQUEST_ID_KEY = "stargate.rid";

    /**
     * async异步标志
     */
    public static final String ASYNC_KEY = "async";

    /**
     * 标示框架内部Filter使用参数的key前缀
     */
    public static final String FILTER_CONTEXT_KEY_PREFIX = "stargate.filter.";

    /**
     * rpc状态的过期时间key,用于失效检查
     */
    public static final String STATUS_EXPIRE_TIME_KEY = "status.expire.time";

    /**
     * rpc状态的默认过期时间60分钟
     */
    public static final long DEFAULT_STATUS_EXPIRE_TIME = 60 * 60 * 1000;

    /**
     * 服务端的并发数限制key
     */
    public static final String EXECUTES_KEY = "executes";

    /**
     * 客户端的并发数限制key
     */
    public static final String ACTIVES_KEY = "actives";

    /**
     * 服务失效的失败计数key
     */
    public static final String FAILED_CHECK_KEY = "failed.check";

    /**
     * 服务失效的默认失败计数3次,过期时间(STATUS_EXPIRE_TIME_KEY)内失败3次,即失效
     */
    public static final int DEFAULT_FAILED_CHECK = 3;

    /**
     * 用于小流量名单规则的缓存超时时间,默认为30分钟
     */
    public static final long DEFAULT_FILE_CACHE_EXPIRE_TIME = 30 * 60 * 1000;

    /**
     * 客户端并发受限时的等待超时时间key
     */
    public static final String ACTIVE_LIMIT_TIMEOUT_KEY = "active.timeout";

    /**
     * 客户端并发受限时的默认等待超时时间,不等待
     */
    public static final long DEFAULT_ACTIVE_LIMIT_TIMEOUT = 0;

    /**** Filter相关参数 ****/
    /**
     * filter配置--标示默认filter组合
     */
    public static final String DEFAULT_KEY = "default";

    /**
     * filter配置--标示删除某种filter
     */
    public static final String REMOVE_VALUE_PREFIX = "-";

    /**
     * filter配置--默认服务端的filter配置组合,增加新的filter需要在此处配置
     */
    public static final String DEFAULT_SERVER_FILTERS = "servercontext,executelimit,servermonitor,remoteaccess";

    /**
     * filter配置--默认客户端的filter配置组合,增加新的filter需要在此处配置
     */
    public static final String DEFAULT_CLIENT_FILTERS = "clientcontext,activelimit,clientmonitor";

    /**
     * 用于配置客户端filter的key
     */
    public static final String CLIENT_FILTER_KEY = "client.filter";

    /**
     * 用于配置服务端filter的key
     */
    public static final String SERVER_FILTER_KEY = "server.filter";

    /**
     * 默认配置文件名的key
     */
    public static final String STARGATE_PROPERTIES_KEY = "stargate.properties.file";

    /**
     * 默认配置文件名
     */
    public static final String DEFAULT_STARGATE_PROPERTIES = "stargate.properties";

    /**
     * 默认配置中心的配置文件名的key
     */
    public static final String CONFCENTER_PROPERTIES_KEY = "confcenter.properties.file";

    /**
     * 默认配置中心的配置文件名
     */
    public static final String DEFAULT_CONFICENTER_PROPERTIES = "confcenter.properties";
    /**
     * 默认使用的配置类型的key
     */
    public static final String CONFIG_TYPE_KEY = "conf.type";
    /**
     * 默认使用的配置类型是本地文件
     */
    public static final String DEFAULT_CONFIG_TYPE = "local";

    //Netty 业务逻辑执行器相关配置

    /**
     * Netty Server业务逻辑执行器的配置前缀 
     */
    public static final String NETTYSERVER_BIZ_EXECUTOR_PREFIX = "nettyserver.bizexecutor.";

    /**
     * Netty Client业务逻辑执行器的配置前缀 
     */
    public static final String NETTYCLIENT_BIZ_EXECUTOR_PREFIX = "nettyclient.bizexecutor.";
    /**
     * Netty Server 业务逻辑执行器的线程池大小 key
     */
    public static final String NETTYSERVER_BIZ_EXECUTOR_POOL_SIZE_KEY = NETTYSERVER_BIZ_EXECUTOR_PREFIX + "pool.size";

    /**
     * Netty Server 业务逻辑执行器的线程池大小  默认值 CPU核数*8
     */
    public static final int DEFAULT_NETTYSERVER_BIZ_EXECUTOR_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 8;

    /**
     * Netty Client 业务逻辑执行器的线程池大小 key
     */
    public static final String NETTYCLIENT_BIZ_EXECUTOR_POOL_SIZE_KEY = NETTYCLIENT_BIZ_EXECUTOR_PREFIX + "pool.size";

    /**
     * Netty Client 业务逻辑执行器的线程池大小  默认值 CPU核数*4
     */
    public static final int DEFAULT_NETTYCLIENT_BIZ_EXECUTOR_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 4;

    /**
     * Netty Server 业务逻辑执行器的总占用最大内存值 key
     */
    public static final String NETTYSERVER_BIZ_EXECUTOR_MAX_TOTAL_MEM_KEY = NETTYSERVER_BIZ_EXECUTOR_PREFIX
            + "max.total.mem";

    /**
     * Netty Server 业务逻辑执行器的总占用最大内存值 默认值 JVM堆内存的20%
     */
    public static final long DEFAULT_NETTYSERVER_BIZ_EXECUTOR_MAX_TOTAL_MEM = Runtime.getRuntime().maxMemory() / 5;
    /**
     * Netty Client 业务逻辑执行器的总占用最大内存值 key
     */
    public static final String NETTYCLIENT_BIZ_EXECUTOR_MAX_TOTAL_MEM_KEY = NETTYCLIENT_BIZ_EXECUTOR_PREFIX
            + "max.total.mem";

    /**
     * Netty Client 业务逻辑执行器的总占用最大内存值 默认值 JVM堆内存的10%
     */
    public static final long DEFAULT_NETTYCLIENT_BIZ_EXECUTOR_MAX_TOTAL_MEM = Runtime.getRuntime().maxMemory() / 10;

    /**
     * Netty Server 业务逻辑执行器的Channel占用最大内存值 key
     */
    public static final String NETTYSERVER_BIZ_EXECUTOR_MAX_CHANNEL_MEM_KEY = NETTYSERVER_BIZ_EXECUTOR_PREFIX
            + "max.channel.mem";
    /**
     * Netty Server 业务逻辑执行器的Channel占用最大内存值 默认值 同总占用内存值
     */
    public static final long DEFAULT_NETTYSERVER_BIZ_EXECUTOR_MAX_CHANNEL_MEM = DEFAULT_NETTYSERVER_BIZ_EXECUTOR_MAX_TOTAL_MEM;

    /**
     * Netty Client 业务逻辑执行器的Channel占用最大内存值 key
     */
    public static final String NETTYCLIENT_BIZ_EXECUTOR_MAX_CHANNEL_MEM_KEY = NETTYCLIENT_BIZ_EXECUTOR_PREFIX
            + "max.channel.mem";
    /**
    * Netty Client 业务逻辑执行器的Channel占用最大内存值 默认值 同总占用内存值
    */
    public static final long DEFAULT_NETTYCLIENT_BIZ_EXECUTOR_MAX_CHANNEL_MEM = DEFAULT_NETTYCLIENT_BIZ_EXECUTOR_MAX_TOTAL_MEM;
    /**
     * Netty Server 业务逻辑执行器的线程活跃时间 key 单位毫秒
     */
    public static final String NETTYSERVER_BIZ_EXECUTOR_KEEP_ALIVE_TIME_KEY = NETTYSERVER_BIZ_EXECUTOR_PREFIX
            + "keep.alive.time";
    /**
     * Netty Server 业务逻辑执行器的线程活跃时间 默认值 30秒
     */
    public static final int DEFAULT_NETTYSERVER_BIZ_EXECUTOR_KEEP_ALIVE_TIME = 30 * 1000;
    /**
     * Netty Client 业务逻辑执行器的线程活跃时间 key 单位毫秒
     */
    public static final String NETTYCLIENT_BIZ_EXECUTOR_KEEP_ALIVE_TIME_KEY = NETTYCLIENT_BIZ_EXECUTOR_PREFIX
            + "keep.alive.time";

    /**
     * Netty Client 业务逻辑执行器的线程活跃时间 默认值 30秒
     */
    public static final int DEFAULT_NETTYCLIENT_BIZ_EXECUTOR_KEEP_ALIVE_TIME = 30 * 1000;

    /**
     * Server端telnet界面端口 key
     */
    public static final String TELNET_PORT_KEY = "telnet.port";

    /**
     * Server端telnet界面端口 默认值 12321
     */
    public static final int DEFAULT_TELNET_PORT = 12321;

    /**
     * instance id key
     */
    public static final String INSTANCE_ID_KEY = "instance.id";

    /**
     * instance id 默认值0
     */
    public static final String DEFAULT_INSTANCE_ID = "0";

    /**
     * 监控日志队列名称
     */
    public static final String MONITOR_LOG_QUEUE_NAME = "MonitorLog";

    /**
     * JPaaS Host Key
     */
    public static final String JPAAS_HOST_KEY = "JPAAS_HOST";

    /**
     * JPaaS Host Port Key
     */
    public static final String JPAAS_PORT_KEY = "JPAAS_HOST_PORT_";

    /**
     * 注册中心id key
     */
    public static final String REGISTRY_ID_KEY = "registry.id";

    /**
     * 注册中心默认id
     */
    public static final String DEFAULT_REGISTRY_ID = "default";
    
    /**
     * 为adcore optfrom字段引入的config，会设置到rpccontext里
     */
    public static final String OPTFROM_CONFIG_KEY = "stargate.optfrom";
    
    /**
     * 这个字段和adcore中的RpcContextDict.OPTFROM是一致的，定制需求
     */
    public static final String OPTFROM_CONTEXT_KEY = "OPTFROM";
}
