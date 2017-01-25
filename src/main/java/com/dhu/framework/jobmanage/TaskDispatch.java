package com.dhu.framework.jobmanage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dhu.framework.conf.EnvConst;

/**
 * 用于定时任务是否进行远程调度
 * @author guofei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TaskDispatch {
	
	/**
	 * 排他锁的锁定时间 单位为秒
	 * 0或小于0 表示 方法执行完(或异常)就释放
	 * 若由于系统异常没有收到结束消息5分钟释放
	 * 默认10分钟释放锁(解决多节点时间差问题)
	 */
	int lockTimes() default 600;
	
	/**
	 * 需要进行远程调度的运行环境,多个环境,分隔
	 * 为空表示所有环境都调度
	 */
	String dispatchEnv() default EnvConst.PRODUCTION;
	
	/**
	 * 当没有获取锁重试次数 每10秒重试一次
	 * 默认10次,一般情况下不需要修改这个值
	 */
	int repeatCount() default 10;
	
}
