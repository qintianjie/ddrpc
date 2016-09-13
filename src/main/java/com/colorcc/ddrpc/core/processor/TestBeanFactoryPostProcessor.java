package com.colorcc.ddrpc.core.processor;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.StringUtils;

import com.colorcc.ddrpc.core.annotation.DdrpcClassPathMapperScanner;

//@PropertySource("classpath:/zk.properties")
//@Configuration
//@Import(DdrpcConfig.class) 
public class TestBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware, PriorityOrdered {
	@Value("${com.colorcc.sample.service}")
	private String basePackage;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placehodlerConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
//	@Autowired
//	DdrpcConfig ddrpcConfig;
	
	private boolean addToConfig = true;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;

	private String beanName;
	private ApplicationContext applicationContext;

	private boolean processPropertyPlaceHolders;

	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
		
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (this.processPropertyPlaceHolders) {
		}
		processPropertyPlaceHolders();

		DdrpcClassPathMapperScanner scanner = new DdrpcClassPathMapperScanner(registry);
		scanner.setAddToConfig(this.addToConfig);
		scanner.setAnnotationClass(this.annotationClass);
		scanner.setMarkerInterface(this.markerInterface);
		scanner.setResourceLoader(this.applicationContext);
		scanner.registerFilters();

//		String[] scanPackages = StringUtils.tokenizeToStringArray(ddrpcConfig.getBasePackage(), ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		String[] scanPackages = StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		scanner.doScan(scanPackages);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		System.out.println("package: " + ddrpcConfig.getBasePackage() + ", basePackage: " + basePackage);
		System.out.println("package: " + ", basePackage: " + basePackage);
//		notNull(this.basePackage, "Property 'basePackage' is required");
	}

	private void processPropertyPlaceHolders() {
		Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		if (!prcs.isEmpty() && applicationContext instanceof GenericApplicationContext) {
			BeanDefinition mapperScannerBean = ((GenericApplicationContext) applicationContext).getBeanFactory().getBeanDefinition(beanName);

			// PropertyResourceConfigurer does not expose any methods to
			// explicitly perform
			// property placeholder substitution. Instead, create a BeanFactory
			// that just
			// contains this mapper scanner and post process the factory.
			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			factory.registerBeanDefinition(beanName, mapperScannerBean);

			for (PropertyResourceConfigurer prc : prcs.values()) {
				prc.postProcessBeanFactory(factory);
			}

//			PropertyValues values = mapperScannerBean.getPropertyValues();

//			this.basePackage = updatePropertyValue("basePackage", values);
			// this.sqlSessionFactoryBeanName =
			// updatePropertyValue("sqlSessionFactoryBeanName", values);
			// this.sqlSessionTemplateBeanName =
			// updatePropertyValue("sqlSessionTemplateBeanName", values);
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

//	private String updatePropertyValue(String propertyName, PropertyValues values) {
//		PropertyValue property = values.getPropertyValue(propertyName);
//
//		if (property == null) {
//			return null;
//		}
//
//		Object value = property.getValue();
//
//		if (value == null) {
//			return null;
//		} else if (value instanceof String) {
//			return value.toString();
//		} else if (value instanceof TypedStringValue) {
//			return ((TypedStringValue) value).getValue();
//		} else {
//			return null;
//		}
//	}

}

///**
// * {@link EnableAutoConfiguration Auto-configuration} for Spring JMS.
// *
// * @author Greg Turnquist
// * @author Stephane Nicoll
// */
//@Configuration
//@ConditionalOnClass(JmsTemplate.class)
//@ConditionalOnBean(ConnectionFactory.class)
//@EnableConfigurationProperties(JmsProperties.class)
//@Import(JmsAnnotationDrivenConfiguration.class)
//public class JmsAutoConfiguration {
//
//	@Configuration
//	protected static class JmsTemplateConfiguration {
//
//		private final JmsProperties properties;
//
//		private final ObjectProvider<DestinationResolver> destinationResolver;
//
//		private final ObjectProvider<MessageConverter> messageConverter;
//
//		public JmsTemplateConfiguration(JmsProperties properties,
//				ObjectProvider<DestinationResolver> destinationResolver,
//				ObjectProvider<MessageConverter> messageConverter) {
//			this.properties = properties;
//			this.destinationResolver = destinationResolver;
//			this.messageConverter = messageConverter;
//		}
//
//		@Bean
//		@ConditionalOnMissingBean
//		@ConditionalOnSingleCandidate(ConnectionFactory.class)
//		public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
//			JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//			jmsTemplate.setPubSubDomain(this.properties.isPubSubDomain());
//			DestinationResolver destinationResolver = this.destinationResolver
//					.getIfUnique();
//			if (destinationResolver != null) {
//				jmsTemplate.setDestinationResolver(destinationResolver);
//			}
//			MessageConverter messageConverter = this.messageConverter.getIfUnique();
//			if (messageConverter != null) {
//				jmsTemplate.setMessageConverter(messageConverter);
//			}
//			return jmsTemplate;
//
//		}
//	}
//
//	@ConditionalOnClass(JmsMessagingTemplate.class)
//	@Import(JmsTemplateConfiguration.class)
//	protected static class MessagingTemplateConfiguration {
//
//		@Bean
//		@ConditionalOnMissingBean
//		@ConditionalOnSingleCandidate(JmsTemplate.class)
//		public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
//			return new JmsMessagingTemplate(jmsTemplate);
//		}
//
//	}
//
//}

//
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
//@Configuration
//@ConditionalOnWebApplication
//@Import(BeanPostProcessorsRegistrar.class)
//public class EmbeddedServletContainerAutoConfiguration {
//
//	/**
//	 * Nested configuration if Tomcat is being used.
//	 */
//	@Configuration
//	@ConditionalOnClass({ Servlet.class, Tomcat.class })
//	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
//	public static class EmbeddedTomcat {
//
//		@Bean
//		public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
//			return new TomcatEmbeddedServletContainerFactory();
//		}
//
//	}
//
//	/**
//	 * Nested configuration if Jetty is being used.
//	 */
//	@Configuration
//	@ConditionalOnClass({ Servlet.class, Server.class, Loader.class,
//			WebAppContext.class })
//	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
//	public static class EmbeddedJetty {
//
//		@Bean
//		public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
//			return new JettyEmbeddedServletContainerFactory();
//		}
//
//	}
//
//	/**
//	 * Nested configuration if Undertow is being used.
//	 */
//	@Configuration
//	@ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
//	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
//	public static class EmbeddedUndertow {
//
//		@Bean
//		public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory() {
//			return new UndertowEmbeddedServletContainerFactory();
//		}
//
//	}
//
//	/**
//	 * Registers a {@link EmbeddedServletContainerCustomizerBeanPostProcessor}. Registered
//	 * via {@link ImportBeanDefinitionRegistrar} for early registration.
//	 */
//	public static class BeanPostProcessorsRegistrar
//			implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
//
//		private ConfigurableListableBeanFactory beanFactory;
//
//		@Override
//		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//			if (beanFactory instanceof ConfigurableListableBeanFactory) {
//				this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
//			}
//		}
//
//		@Override
//		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
//				BeanDefinitionRegistry registry) {
//			if (this.beanFactory == null) {
//				return;
//			}
//			if (ObjectUtils.isEmpty(this.beanFactory.getBeanNamesForType(
//					EmbeddedServletContainerCustomizerBeanPostProcessor.class, true,
//					false))) {
//				registry.registerBeanDefinition(
//						"embeddedServletContainerCustomizerBeanPostProcessor",
//						new RootBeanDefinition(
//								EmbeddedServletContainerCustomizerBeanPostProcessor.class));
//
//			}
//			if (ObjectUtils.isEmpty(this.beanFactory.getBeanNamesForType(
//					ErrorPageRegistrarBeanPostProcessor.class, true, false))) {
//				registry.registerBeanDefinition("errorPageRegistrarBeanPostProcessor",
//						new RootBeanDefinition(
//								ErrorPageRegistrarBeanPostProcessor.class));
//
//			}
//		}
//
//	}
//
//}
