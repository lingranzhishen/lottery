package com.dhu.framework.cache;

/**
 * 通用缓存接口
 * 实现类:ehcacheManager,memcacheManager,rediscacheManager
 * @author guofei
 *
 */
public interface CacheManager {
	
	public static final String EHCACHE = "ehcacheManager";
	
	public static final String MEMCACHE = "memcacheManager";
	
	public static final String REDIS = "rediscacheManager";
	
	public static final String DEFAULT = CacheManager.MEMCACHE;
	
	/**
	 * put和add的差别
	 * 当key已存在时put覆盖,add不覆盖返回false
	 * @param key 缓存key
	 * @param value 缓存对象
	 * @param timeToLive 存活时间
	 */
	public void put(String key, Object value, int timeToLive);
	/**
	 * put和add的差别
	 * 当key已存在时put覆盖,add不覆盖返回false
	 * @param key 缓存key
	 * @param value 缓存对象
	 * @param timeToLive 存活时间
	 */
	public boolean add(String key, Object value, int timeToLive);
	/**
	 * 更新存活时间,相当于实现活跃时间
	 * @param key 
	 * @param timeToLive
	 * @return 缓存对象
	 */
	public Object getAndTouch(String key,int timeToLive);
	
	public Object get(String key);

	public void delete(String key);
	
	public boolean containsKey(String key);
	
	/**
	 * 递增统计数据
	 * @param key 缓存key
	 * @param delta 需要增加的数值
	 * @param timeToLive 缓存存活时间
	 * @return 成功的话返回当前缓存值,失败返回-1
	 */
	public long incr(String key,long delta, int timeToLive);
	
}
