package com.dhu.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 区分城市拦截器
 * @author Fant
 */
public class CityInteceptor extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String serverName = request.getServerName().substring(6);//agent.sh.dhu.com
		String cityCode = Constants.DEFAULT_CITY;
		int index = serverName.indexOf(".");
		if(index > 1){
			String newCityCode = serverName.substring(0, index);
			if(Constants.ALL_CITY.contains(newCityCode)){
				cityCode = newCityCode;
			}
		}
		CityThreadLocal.set(cityCode);
		
		return true;
	}
	
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
