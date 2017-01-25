package com.dhu.framework.cache.impl;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhu.framework.cache.CacheManager;

public class MemCacheManager implements CacheManager {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MemcachedClient ljmemcachedClient;
	

	@Override
	public void put(String key, Object value, int timeToLive) {
		if (key == null || value == null)
			return;
		if(ljmemcachedClient==null){
			log.debug("ljmemcachedClient is null");
			return;
		}
		try {
			ljmemcachedClient.set(key, timeToLive, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean add(String key, Object value, int timeToLive) {
		if (key == null || value == null)
			return false;
		if(ljmemcachedClient==null){
			log.debug("ljmemcachedClient is null");
			return false;
		}
		try {
			return ljmemcachedClient.add(key, timeToLive, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public Object getAndTouch(String key, int timeToLive) {
		if (key == null)
			return null;
		if(ljmemcachedClient==null){
			log.debug("ljmemcachedClient is null");
			return null;
		}
		try {
			return ljmemcachedClient.getAndTouch(key, timeToLive);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Object get(String key) {
		if (key == null)
			return null;
		if(ljmemcachedClient==null){
			log.debug("ljmemcachedClient is null");
			return null;
		}
		try {
			return ljmemcachedClient.get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void delete(String key) {
		if (key == null)
			return;
		if(ljmemcachedClient==null){
			log.debug("ljmemcachedClient is null");
			return;
		}
		try {
			ljmemcachedClient.delete(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean containsKey(String key) {
		if (key == null)
			return false;
		if(ljmemcachedClient==null){
			log.debug("ljmemcachedClient is null");
			return false;
		}
		try {
			return (ljmemcachedClient.get(key) != null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public long incr(String key, long delta, int timeToLive) {
		if (key == null)
			return -1;
		if(ljmemcachedClient==null){
			log.debug("ljmemcachedClient is null");
			return -1;
		}
		try {
			return ljmemcachedClient.incr(key, delta, delta,2000,timeToLive);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return -1;
		}
	}

}
