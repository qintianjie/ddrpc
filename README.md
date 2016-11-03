基于 SpringBoot 的 Rpc 调用框架
=================================
基于 SpringBoot 框架，实现一个 rpc 框架。 其特点是：
* 启动简单。  main() 函数启动，最简单形式。
* 已有系统可以快速集成到该 rpc 框架。   通过扫描接口，生成其代理后请求具体实现。 对有接口层的系统，无需做任何业务修改。
* 使用  & 配置简单。 基本功能仅需定义如下 config 即可 (其中 @DdrpcScan 指定要对外提供服务的 package 即可):
	``` Java
		@Configuration
		@ConditionalOnProperty(prefix = "ddrpc", name = "enabled", havingValue = "true", matchIfMissing = true)
		@DdrpcScan("com.colorcc.sample.service")
		public class DdrpcConfig {
			
			@Bean
			public DdrpcFactoryBean ddrpcFactoryBean() {
				return new DdrpcFactoryBean();
			}
		
		}
	```
限制
--
1. 依赖 SpringBoot框架。
2. 针对接口编程，将接口服务提供出去。

Update Logs
----
<b>20161103</b>  
	前后端跑通。 不过 method -> registry 未考虑 parameterTypes ，待解决   
	Provider & Consumer 通过RPC方式跑通。 需要优化的地方为 method 存入 methodcache 要考虑同名不同参数区分。类型参数需要转换成 对象类型， Date <---> long / string 需要特殊处理。 
	@TODO 优化代码结构
	
	目前偷懒，把 provider & consumer 的启动类在一个项目里。 如果各自独立项目，不需要如下烦琐操作。  
	Provider 启动： 
		com.colorcc.ddrpc.sample.config.provider.ProviderDdrpcConfig 文件annotation 生效   
		com.colorcc.ddrpc.sample.config.consumer.ConsumerDdrpcConfig 文件annotation 无效  
		执行com.colorcc.ddrpc.ColorccDdrpcApplication 看到 server start.  
	Consumer 启动 & 测试：  
		com.colorcc.ddrpc.sample.config.provider.ProviderDdrpcConfig 文件annotation 无效  
		com.colorcc.ddrpc.sample.config.consumer.ConsumerDdrpcConfig 文件annotation 生效  
		执行com.colorcc.ddrpc.ClientApplication 看到  consumer 执行结果。
	
	

<b>20161102</b>  
	完善 server 端 service 实例化，同时启动 netty server 功能  
	  
	
<b>20161027</b>  
	@todo  
	Add serviceProxy  & filter  
	Real cient & server in main package  
	
<b>20161026</b>  
	Netty sample 完善,见 test package 
	RPC实现流程思考:  
	Client端 根据 service --生成--> proxyImpl   
	Server端 根据 service --找到--> serviceImpl  
	统一封装 client / server 的 Impl 到 一个新的 proxy, 这个 proxy 可以实现 loadbalance, monitor... 等功能.  
	
	
<b>20161019</b>  
	Netty 范例代码跑通
	NettyClient 开发,考虑通过 callback 机制返回 respose 供 consumer 端处理.  Done  
	callback implements Future 达到异步获取结果的效果.
	
<b>20161017</b>  
	Netty 代码部分提交

<b>20161010</b>  
	代码保存,存在编译错误.
	
	@TODO:  
		1. Service 对应 proxy 生成. 简单点考虑 jdk 自动生成,复杂考虑 javassist 写公用 proxy  
		2. Client 端将 service 的 每个 method 生成 Method对象,根据 methodName, params type/value, return type 等将 Method 对象 encoder 成 ByteBuf 准备 TCP 网络传输.  
		3. Client 端生成 proxy同时,需创建 netty client 并在 channelActive 时将 ByteBuf 发出去.  
		4. Server 端生成 proxy对象,这里是根据 service type + ref object 生成,因此真正的service method invoke 就是 ref object 的 method invoke, 从而达到方法在对象上执行.  
		5. Server 端创建 netty server,根据步骤 3　接受 tcp ByteBuf,在 channelRead 方法对其进行 decoder(msg),得到的是客户端 Method　对象属性,根据该对象在步骤2提供的属性,找到 ref object　对应的方法,将步骤2的 valuec传入 ref object　方法执行.  
		6.　如有必要,ref object　执行结果在 netty server进行 encoder->ByteBuf　并 writeAndFlush　到 Buffer　进行发送.  
		7.　Client端 netty client　有个 channelHandler　方法 channelRead　得到 response,进行 decoder　后可继续处理最终结果.  
		
<b>20160919</b>  
	代码重构，添加注解。
	目前 project 分 com.colorcc.ddrpc.core 和 com.colorcc.sample 两个部分。 前者是框架，后者是 sample.
	针对整个 sample，如果已分为 service -> serviceImpl 这样接口实现。 则只要添加 sample.config.DdrpcConfig即可。
	   否则，需要先将项目改造成按接口实现，再添加 DdrpcConfig 配置
	@Will Do: logger & javassist proxy

<b>20160919</b>  
	重构了代码，去掉硬编码功能。 接口在使用代理时，通过 applicationContext get到具体的 spring 定义的 bean 执行。
	修改Java7环境依赖， type.getAnnotationsByType(DdrpcService.class) 只能用在 Java8 中
	使用自定义 beanName  （ Service.class.getName() ）
	@Will Do: package refactor ...
    
<b>20160914</b>  
    Test jenkins auto build

<b>20160914</b>  
	Demo 版完成，具有最基本的功能。 可根据 interface 在 proxy 中调用其具体实现的方法。
