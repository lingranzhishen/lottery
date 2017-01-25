package com.dhu.common;

import com.dhu.portal.model.UserInfo;

/**
 * 登陆员工的本地线程
 * @author guofei
 */
public class UserThreadLocal{

	final static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();
	
	public static void setUser(UserInfo user){
		threadLocal.set(user);
	}
	
	public static UserInfo getUser(){
		return threadLocal.get();
	}
	
	public static void remove(){
		threadLocal.remove();
	}
}
