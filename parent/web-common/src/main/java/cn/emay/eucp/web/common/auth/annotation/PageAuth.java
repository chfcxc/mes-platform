package cn.emay.eucp.web.common.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作权限,页面级别
 * 
 * @author Frank
 *
 */
@Target({ ElementType.TYPE,ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageAuth {

	/**
	 * 页面权限
	 */
	String[] value();

}
