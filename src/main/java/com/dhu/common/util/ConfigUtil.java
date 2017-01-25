package com.dhu.common.util;

import java.util.HashMap;
import java.util.Map;

import com.dhu.common.CityThreadLocal;
import com.dhu.common.Constants;
import com.dhu.common.ResourceConfig;
import com.dhu.framework.conf.GlobalConfig;
import com.dhu.portal.enums.CityNameEnum;

/**
 * 配置参数初始化
 * 
 * @author Fant
 */
public class ConfigUtil {

	private static Map<String, Map<String, Object>> configMap = null;

	public static Map<String, Object> getConfigMap() {
		if (configMap == null) {
			initConfigMap();
		}
		return configMap.get(CityThreadLocal.get());
	}

	/**
	 * 初始化configMap
	 */
	private synchronized static void initConfigMap() {
		if (configMap == null) {
		}
	}
}
