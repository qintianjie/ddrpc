package com.colorcc.ddrpc.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
//import org.mybatis.spring.annotation.MapperScannerRegistrar;
//import org.mybatis.spring.mapper.MapperFactoryBean;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DdrpcScannerRegistrar.class)
public @interface DdrpcScan {
/**
	   * Alias for the {@link #basePackages()} attribute. Allows for more concise
	   * annotation declarations e.g.:
	   * {@code @EnableMyBatisMapperScanner("org.my.pkg")} instead of {@code
	   * @EnableMyBatisMapperScanner(basePackages= "org.my.pkg"})}.
	   */
	String[] value() default {};

	/**
	 * Base packages to scan for MyBatis interfaces. Note that only interfaces
	 * with at least one method will be registered; concrete classes will be
	 * ignored.
	 */
	String[] basePackages() default {};

	/**
	 * Type-safe alternative to {@link #basePackages()} for specifying the
	 * packages to scan for annotated components. The package of each class
	 * specified will be scanned.
	 * <p>
	 * Consider creating a special no-op marker class or interface in each
	 * package that serves no purpose other than being referenced by this
	 * attribute.
	 */
	Class<?>[] basePackageClasses() default {};

	/**
	 * This property specifies the annotation that the scanner will search for.
	 * <p>
	 * The scanner will register all interfaces in the base package that also
	 * have the specified annotation.
	 * <p>
	 * Note this can be combined with markerInterface.
	 */
	Class<? extends Annotation> annotationClass() default Annotation.class;

	/**
	 * This property specifies the parent that the scanner will search for.
	 * <p>
	 * The scanner will register all interfaces in the base package that also
	 * have the specified interface class as a parent.
	 * <p>
	 * Note this can be combined with annotationClass.
	 */
	Class<?> markerInterface() default Class.class;

	/**
	 * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring
	 * bean.
	 *
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends DdrpcFactoryBean> factoryBean() default DdrpcFactoryBean.class;

}
