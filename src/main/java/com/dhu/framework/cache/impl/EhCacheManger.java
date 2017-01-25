package com.dhu.framework.cache.impl;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.dhu.framework.cache.CacheManager;

public class EhCacheManger implements CacheManager {
	
	@Autowired
	private Ehcache ljehcache;
	
	@Override
	public void put(String key, Object value, int timeToLive) {
		if (key == null || value == null)
			return;
		ljehcache.put(new Element(key, value,null,0,timeToLive));
	}

	@Override
	public boolean add(String key, Object value, int timeToLive) {
		if (key == null || value == null)
			return false;
		return ljehcache.putIfAbsent(new Element(key, value,null,0,timeToLive))==null;
	}

	@Override
	public Object get(String key) {
		if (key == null)
			return null;
		Element element = ljehcache.get(key);
		return element != null ? element.getObjectValue() : null;
	}
	
	@Override
	public Object getAndTouch(String key, int timeToIdle) {
		if (key == null)
			return null;
		Element element = ljehcache.get(key);
		if (element != null) {
			if (element.getTimeToIdle() != timeToIdle) {
				element.setTimeToIdle(timeToIdle);
				ljehcache.put(element);
			}
			return element.getObjectValue();
		}
		return null;
	}

	@Override
	public void delete(String key) {
		if (key == null)
			return;
		ljehcache.remove(key);
	}

	@Override
	public boolean containsKey(String key) {
		if (key == null)
			return false;
		if (ljehcache != null)
			return ljehcache.isKeyInCache(key);
		else
			return false;
	}

	@Override
	public long incr(String key, long delta, int timeToLive) {
		if (key == null || delta == 0)
			return -1;
		if (ljehcache != null) {
			Element element = ljehcache.putIfAbsent(new Element(key,new Long(delta),null,0,timeToLive));
			if (element == null) {
				return delta;
			} else {
				long value = ((long) element.getObjectValue()) + delta;
				ljehcache.put(new Element(key, new Long(value),null,0,timeToLive));
				return value;
			}
		} else
			return -1;
	}
}
