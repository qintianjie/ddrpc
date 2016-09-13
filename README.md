基于 SpringBoot 的 Rpc 调用框架
=================================
基于 SpringBoot 框架，实现一个 rpc 框架。 其特点是：
* 启动简单。  main() 函数启动，最近简单形式。
* 已有系统可以快速集成到该 rpc 框架。   通过扫描接口，生成其代理后请求具体实现。 对有接口层的系统，无需做任何业务修改。
* 使用  & 配置简单。 基本功能仅需定义如下 config 即可 (其中 @DdrpcScan 指定要对外提供服务的 package 即可):
	``` Java
		@Configuration
		@DdrpcScan("com.colorcc.sample.service")
		public class DdrpcConfig {
		
		}
	```
限制
--
1. 依赖 SpringBoot框架。
2. 针对接口编程，将接口服务提供出去。

Update Logs
----
<b>20160914</b>  
	Demo 版完成，具有最基本的功能。 可根据 interface 在 proxy 中调用其具体实现的方法。
