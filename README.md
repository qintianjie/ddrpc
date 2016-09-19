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
