package com.dhu.framework.conf;

import java.util.Properties;
import java.util.Set;

import com.dhu.framework.utils.IPUtil;

/**
 * 读取global.properties文件专用java类
 */
public class GlobalConfig {

	/**
	 * global文件中参数
	 */
	private static String env;

	private static String projectCode;
	
	private static Properties prop;

	private GlobalConfig() {
	}
	
	static {
		prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("global.properties"));
			env = initEnv();
			projectCode = initProjectCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置envirement
	 * 
	 * @param prop
	 *            系统配置对象
	 */
	private static String initEnv() {
		String productionIp = prop.getProperty("production_ip", "");
		String integrationIp = prop.getProperty("integration_ip", "");
		String developmentIp = prop.getProperty("development_ip", "");
		String testIp = prop.getProperty("test_ip", "");
		String previewIp = prop.getProperty("preview_ip", "");
		String tenv = null;
		Set<String> localIps = IPUtil.getLocalIpSet();
		if (containsIp(productionIp, localIps)) {
			tenv = EnvConst.PRODUCTION;
		} else if (containsIp(integrationIp, localIps)) {
			tenv = EnvConst.INTEGRATION;
		} else if (containsIp(testIp, localIps)) {
			tenv = EnvConst.TEST;
		} else if (containsIp(developmentIp, localIps)) {
			tenv = EnvConst.DEVELOPMENT;
		} else if (containsIp(previewIp, localIps)) {
			tenv = EnvConst.PREVIEW;
		} else {
			tenv = prop.getProperty("env", "");
			if (tenv.isEmpty()
					|| (!tenv.equals(EnvConst.DEVELOPMENT)
							&& !tenv.equals(EnvConst.PRODUCTION)
							&& !tenv.equals(EnvConst.PREVIEW)
							&& !tenv.equals(EnvConst.TEST)
							&& !tenv.equals(EnvConst.INTEGRATION))) {
				tenv = EnvConst.DEVELOPMENT;
			}
		}
		return tenv;
	}
	/**
	 * 检查ip设置是否在本地ip内
	 * 
	 * @param ipSet
	 *            global中的ip设置
	 * @param localIps
	 *            本地ip集合
	 * @return boolean
	 */
	private static boolean containsIp(String ipSet, Set<String> localIps) {
		if(ipSet == null || ipSet.isEmpty()) return false;
		
		String[] ips = ipSet.split("\\|\\|");
		for (String ip : ips) {
			for(String local : localIps){
				if(local.startsWith(ip)){
					return true;
				}
			}
		}
		return false;
	}
	
	private static String initProjectCode(){
		String projectCode = prop.getProperty("projectCode", "");
		if(projectCode.isEmpty()){
			projectCode = prop.getProperty("appCode");
		}
		return projectCode;
	}
	/**
	 * 获取环境
	 * @return
	 */
	public static String getEnv(){
		return env;
	}
	/**
	 * 获取配置文件中参数
	 * @return
	 */
	public static String getProjectCode(){
		return projectCode;
	}
	/**
	 * 根据key获取参数
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		return prop.getProperty(key);
	}

}
