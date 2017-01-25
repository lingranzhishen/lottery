package com.dhu.common.util; 

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateException;

/** 
 * @Description: freemarker封装
 */
@Component
public class FreemarkerUtil
{
	@Autowired
	public FreeMarkerConfigurer freeMarkerConfigurer;
	
	/**
	 * 填充模版 
	 * @param template 模版文件名
	 * @return String
	 * @throws IOException
	 */
	public String writeTemplate(String template) throws IOException{
		return freeMarkerConfigurer.getConfiguration().getTemplate(template).toString();
	}
	
	/**
	 * 填充模版
	 * @param template 模版名
	 * @param map 变量map
	 * @return String
	 * @throws TemplateException
	 * @throws IOException
	 */
	public String writeTemplate(String template, Map<String, ?> map) throws TemplateException, IOException {
		Writer writer = new StringWriter();
		freeMarkerConfigurer.getConfiguration().getTemplate(template).process(map, writer);
		return writer.toString();
	}
}
    