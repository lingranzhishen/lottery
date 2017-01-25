package com.dhu.framework.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dhu.framework.cache.CacheManager;

@Target({ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
public @interface CheckCache {
	/**
	 * 相对参数的key 默认是所有参数,内置哈希算法
	 */
	String key() default "";
	/**
	 * 命名空间,不指定默认使用projectCode
	 */
	String namespace() default "";
	
	/**
	 * 存活时间 单位秒
	 * 默认1小时
	 */
	int timeToLive() default 60*60;
	
	/**
	 * 活跃时间 单位秒
	 * 默认永久
	 */
	int timeToIdle() default 0;
	
	/**
	 * 缓存类型
	 * 默认本地缓存
	 */
	String type() default CacheManager.DEFAULT;
	
	/**
	 * 空值是否缓存,防止缓存穿透
	 */
	boolean cacheNull() default true;
	
    /**
     * 自动添加前缀(两位类全名.方法名)
     * @return
     */
    boolean autoKeyPre() default true;
}
