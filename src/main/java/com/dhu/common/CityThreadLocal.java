package com.dhu.common;


/**
 * 城市的本地线程
 */
public class CityThreadLocal {

	final static ThreadLocal<String> threadLocal = new ThreadLocal<>();

	public static void set(String cityCode){
		threadLocal.set(cityCode);
	}

	public static String get(){
		return threadLocal.get();
	}

	public static void remove(){
		threadLocal.remove();
	}
}
