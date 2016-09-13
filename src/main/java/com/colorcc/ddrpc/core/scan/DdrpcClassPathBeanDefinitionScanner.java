package com.colorcc.ddrpc.core.scan;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

public class DdrpcClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

	private String basePackages;
	private Class<? extends Annotation> annotationClass;
	private String myPv;
	

	public String getMyPv() {
		return myPv;
	}

	public void setMyPv(String myPv) {
		this.myPv = myPv;
	}

	public Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public String getBasePackages() {
		return basePackages;
	}

	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

	public DdrpcClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}
	
	@Override
	  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
	    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

	    if (beanDefinitions.isEmpty()) {
	      logger.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
	    } else {
	      processBeanDefinitions(beanDefinitions);
	    }

	    return beanDefinitions;
	  }

	  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
	    GenericBeanDefinition definition;
	    for (BeanDefinitionHolder holder : beanDefinitions) {
	      definition = (GenericBeanDefinition) holder.getBeanDefinition();

	      if (logger.isDebugEnabled()) {
	        logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() 
	          + "' and '" + definition.getBeanClassName() + "' mapperInterface");
	      }
	      
	      if (StringUtils.hasText(this.myPv)) {
	    	  definition.getPropertyValues().add("myPv", this.myPv);
	      }

//	      // the mapper interface is the original class of the bean
//	      // but, the actual class of the bean is MapperFactoryBean
//	      definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
//	      definition.setBeanClass(this.mapperFactoryBean.getClass());
//
//	      definition.getPropertyValues().add("addToConfig", this.addToConfig);
//
//	      boolean explicitFactoryUsed = false;
//	      if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
//	        definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
//	        explicitFactoryUsed = true;
//	      } else if (this.sqlSessionFactory != null) {
//	        definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
//	        explicitFactoryUsed = true;
//	      }
//
//	      if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
//	        if (explicitFactoryUsed) {
//	          logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
//	        }
//	        definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
//	        explicitFactoryUsed = true;
//	      } else if (this.sqlSessionTemplate != null) {
//	        if (explicitFactoryUsed) {
//	          logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
//	        }
//	        definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
//	        explicitFactoryUsed = true;
//	      }
//
//	      if (!explicitFactoryUsed) {
//	        if (logger.isDebugEnabled()) {
//	          logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
//	        }
//	        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//	      }
	    }
	  }

	public void registerFilters() {
		boolean acceptAllInterfaces = true;

		// if specified, use the given annotation and / or marker interface
		if (this.annotationClass != null) {
			addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
			acceptAllInterfaces = false;
		}

		// // override AssignableTypeFilter to ignore matches on the actual
		// marker interface
		// if (this.markerInterface != null) {
		// addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
		// @Override
		// protected boolean matchClassName(String className) {
		// return false;
		// }
		// });
		// acceptAllInterfaces = false;
		// }

		if (acceptAllInterfaces) {
			// default include filter that accepts all classes
			addIncludeFilter(new TypeFilter() {
				@Override
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
					return true;
				}
			});
		}

		// exclude package-info.java
		addExcludeFilter(new TypeFilter() {
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
				String className = metadataReader.getClassMetadata().getClassName();
				return className.endsWith("package-info");
			}
		});
	}
	
	 @Override
	  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
	    return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
	    if (super.checkCandidate(beanName, beanDefinition)) {
	      return true;
	    } else {
	      logger.warn("Skipping MapperFactoryBean with name '" + beanName 
	          + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
	          + ". Bean already defined with the same name!");
	      return false;
	    }
	  }

}
