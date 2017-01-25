package com.dhu.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhu.framework.conf.GlobalConfig;

/*********
 * 配置文件读取通用工具
 * <p>配置文件必须为 key-value的方式保存方可识别
 * @author guofei
 */
public class ResourceConfig {
	
	private static Logger log = LoggerFactory.getLogger(ResourceConfig.class);
	//属性文件 key value Map
    private static Map<String, String> resourceMap = init();
    
    private ResourceConfig() {
    }

    /**
     * 初始化资源Map
     * @return Map
     */
    private static Map<String, String> init() {
        String env = GlobalConfig.getEnv();
        Map<String, String> initMap = new HashMap<String,String>();
        propTransIntoMap(initMap, getProperties(env+".properties"));
        initMap.put("env",env);
        initMap.put("projectCode",GlobalConfig.getProjectCode());
        return initMap;
    }

    private static Properties getProperties(String fileName) {
        Properties prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return prop;
    }
    

    /**
     * @param initMap
     * @param prop
     */
    private static void propTransIntoMap(Map<String, String> initMap,Properties prop) {
        Set<Object> keySet = prop.keySet();
        for (Object key : keySet) {
            String propKey = ((String) key).trim();
            String propValue = prop.getProperty((String) key).trim();
            if(initMap.containsKey(propKey)){
                log.error("'"+propKey+"' is duplicate,place check your properties");
            }
            initMap.put(propKey, propValue);
        }
    }

    
    /**
     * 获取参数值
     * @param key
     * @return 获取配置参数值
     */
    public static String getString(String key) {
    	String value = resourceMap.get(key);
    	return value == null? "":value.trim();
    }
    
    /**
     * 获取int型参数
     * @param key 
     * @return int型参数值
     */
    public static Integer getInt(String key){
    	return Integer.parseInt(getString(key));
    }

    /**
     * 重新加载属性文件
     */
    public static void flush() {
    	Map<String, String> temp = resourceMap;
    	resourceMap = init();
    	temp.clear();
    }
    
    /**
     * 所有参数
     * @return 所有参数
     */
    public static Map<String, String> getAllMap() {
        return resourceMap;
    }
}
