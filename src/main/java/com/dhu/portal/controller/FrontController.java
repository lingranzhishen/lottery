package com.dhu.portal.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhu.common.JsonResult;
import com.dhu.common.ResourceConfig;
import com.dhu.common.base.BaseController;
import com.dhu.common.util.IPUtil;

@Controller
public class FrontController extends BaseController {
	
	/**
	 * 通用页面跳转url
	 */
	@RequestMapping(value = "/front/**", method = RequestMethod.GET)
	public String portal(Model model, HttpServletRequest request,HttpServletResponse resp) {
		String servletPath = request.getServletPath();
		int index = servletPath.indexOf("front/");
		String forwardUrl = servletPath.substring(index + 5);
		
		// 将自定义参数加入
		Map<String, String[]> params = request.getParameterMap();
		for (Entry<String, String[]> entry : params.entrySet()) {
            String value = joinstr(entry.getValue(), ",");
            model.addAttribute(entry.getKey(), value);
        }
		return forwardUrl;
	}
	
	@RequestMapping(value = "/locallogout")
	public String locallogout(HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session != null){
			session.invalidate();
		}
		String url = request.getRequestURL().toString();
		url = url.substring(0,url.indexOf("/",10));
		return redirect(ResourceConfig.getString("cas.serverUrl")+"/logout?service="+url);
	}
	
	@ResponseBody
	@RequestMapping(value="/getsystemconfig.json")
	public JsonResult getsystemconfig(){
		String defaultCharset = Charset.defaultCharset().toString();
		String fileencoding = System.getProperty("file.encoding");
		OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
		String defaultCharsetuse = enc;
		
		return ok().put("defaultCharset", defaultCharset)
				.put("fileencoding", fileencoding)
				.put("defaultCharsetuse",defaultCharsetuse)
				.put("ip",IPUtil.getLocalIp());
	}
	
	private String joinstr(String[] strs,String split){
		StringBuffer sb = new StringBuffer();
		int size = strs.length;
		for(int i=0;i<size;i++){
			if(i > 0){
				sb.append(split);
			}
			sb.append(strs[i]);
		}
		return sb.toString();
	}
}
