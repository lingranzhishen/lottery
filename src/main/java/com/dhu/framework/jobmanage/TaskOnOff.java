package com.dhu.framework.jobmanage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dhu.framework.conf.EnvConst;

/**
 * 定时任务开关,用于控制任务是否可执行
 * @author guofei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TaskOnOff {
	
	/**
	 * 可运行环境
	 * 为空表示所有环境都可运行
	 * 默认为正式环境
	 */
	String canRunEnv() default EnvConst.PRODUCTION;
	
	/**
	 * 可运行的Ip对应的spring环境属性值
	 * 注意:1.此属性需存在于spring容器内
	 */
	String canRunIpProp() default "";
	
}
