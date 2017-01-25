package com.dhu.lottery.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhu.common.base.BaseController;
import com.dhu.framework.cache.CacheManager;
import com.dhu.portal.service.LoginService;

import net.rubyeye.xmemcached.MemcachedClient;

@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController {

	@Autowired
	LoginService loginService;
	
	
	
	@RequestMapping(value = "")
	public String index(HttpServletResponse response){
		return "index";
	}
	
	
}
