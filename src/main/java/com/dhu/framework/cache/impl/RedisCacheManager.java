package com.dhu.framework.cache.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.dhu.framework.cache.CacheManager;

public class RedisCacheManager implements CacheManager {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RedisTemplate<String,Object> ljredisTemplate;

	@Override
	public void put(String key, Object value, int timeToLive) {
		if (key == null || value == null)
			return;
		if(ljredisTemplate==null){
			log.debug("ljredisTemplate is null");
			return;
		}
		try {
			if (timeToLive > 0)
				ljredisTemplate.opsForValue().set(key,value, timeToLive);
			else
				ljredisTemplate.opsForValue().set(key,value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean add(String key, Object value, int timeToLive) {
		if (key == null || value == null)
			return false;
		if(ljredisTemplate==null){
			log.debug("ljredisTemplate is null");
			return false;
		}
		try {
			boolean success = ljredisTemplate.opsForValue().setIfAbsent(key, value);
			if (success && timeToLive > 0){
				ljredisTemplate.expire(key, timeToLive,TimeUnit.SECONDS);
			}
			return success;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public Object getAndTouch(String key, int timeToLive) {
		if (key == null)
			return null;
		if(ljredisTemplate==null){
			log.debug("ljredisTemplate is null");
			return null;
		}
		Object rs = ljredisTemplate.opsForValue().get(key);
		if(rs != null && timeToLive > 0){
			ljredisTemplate.expire(key, timeToLive, TimeUnit.SECONDS);
		}
		return rs;
	}

	@Override
	public Object get(String key) {
		if (key == null)
			return null;
		if(ljredisTemplate==null){
			log.debug("ljredisTemplate is null");
			return null;
		}
		try {
			return ljredisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void delete(String key) {
		if (key == null)
			return;
		if(ljredisTemplate==null){
			log.debug("ljredisTemplate is null");
			return;
		}
		try {
			ljredisTemplate.delete(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean containsKey(String key) {
		if (key == null)
			return false;
		if(ljredisTemplate==null){
			log.debug("ljredisTemplate is null");
			return false;
		}
		try {
			return ljredisTemplate.hasKey(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public long incr(String key, long delta, int timeToLive) {
		if (key == null)
			return -1;
		if(ljredisTemplate==null){
			log.debug("ljredisTemplate is null");
			return -1;
		}
		try {
			long result = ljredisTemplate.opsForValue().increment(key,delta);
			if (timeToLive > 0)
				ljredisTemplate.expire(key, timeToLive, TimeUnit.SECONDS);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return -1;
		}
	}

}
