package com.dhu.portal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhu.common.ResourceConfig;
import com.dhu.portal.model.UserInfo;

/**
 * 登陆、会话管理相关接口
 * @author Fant
 */
@Service
public class LoginService {
	
	private static final Logger log = LoggerFactory.getLogger(LoginService.class);
	@Autowired
    private  RestTemplate restTemplate;
	
	/**
	 * 根据ucId创建token
	 * @param ucId
	 */
	public UserInfo createToken(String ucId){
        return null;
    }
	
	/**
	 * 验证token
	 * @param ucId
	 */
	public UserInfo verifyToken(String token){
        String url= ResourceConfig.getString("login.host");
        url = url + "/token/verify";
        MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
        request.add("token", token);
        try {
        	String object = restTemplate.postForObject(url, request, String.class);
            JSONObject json = JSON.parseObject(object);
            if(json.getIntValue("error_code") == 0){
            	JSONObject data = json.getJSONObject("data");
            	JSONObject userInfo = data.getJSONObject("user_info");
            	UserInfo agent = JSON.toJavaObject(userInfo, UserInfo.class);
            	agent.setToken(data.getString("token"));
            	return agent;
            }
		} catch (Exception e) {
			log.error("创建token失败：", e);
		}
        return null;
    }

}
