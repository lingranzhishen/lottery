package com.dhu.framework.cache;

import com.dhu.framework.conf.GlobalConfig;

public class CacheUtil {
	
	public static String getCacheKey(String key){
		return getCacheKey(GlobalConfig.getProjectCode(),key);
	}
	/**
	 * 拼接前缀
	 * @param key
	 */
	public static String getCacheKey(String namespace,String key){
		if(namespace == null || namespace.equals("")){
			return key;
		}
		return namespace+"_"+key;
	}
}
