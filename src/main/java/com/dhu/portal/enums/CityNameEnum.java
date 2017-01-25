package com.dhu.portal.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 标签枚举
 * 
 * @author Fant
 */
public enum CityNameEnum {

	SH("上海"), SU("苏州");

	private String cityName;

	CityNameEnum(String gbCode) {
		this.cityName = gbCode;
	}

	public static final Map<String, String> cityNameMap = new HashMap<String, String>();

	/**
	 * 当前枚举的小写值
	 * 
	 * @return
	 */
	public String value() {
		return this.name().toLowerCase();
	}

	static {
		for (CityNameEnum cce : CityNameEnum.values()) {
			cityNameMap.put(cce.value(), cce.cityName);
		}
	}
	
	
}
