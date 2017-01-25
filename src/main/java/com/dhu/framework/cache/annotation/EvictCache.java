package com.dhu.framework.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dhu.framework.cache.CacheManager;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EvictCache {
    /**
     * 相对参数的key
     */
    String key();
    
    /**
	 * 命名空间,不指定默认使用projectCode
	 */
	String namespace() default "";

    /**
     * 缓存类型
     * 默认本地缓存
     */
    String type() default CacheManager.DEFAULT;
}