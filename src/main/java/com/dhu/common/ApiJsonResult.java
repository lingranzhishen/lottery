package com.dhu.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dhu.common.util.StringUtil;

/**
 * 返回json的标准格式
 * {errno:0, error:'test', data:{}}
 */
public class ApiJsonResult extends HashMap<String,Object>{
    
	private static final long serialVersionUID = -1702618129121801017L;

	private static enum STATUS {OK, FAIL};
	private static final int ERRNO_OK = 0;//返回成功
	
	public static final int ERRNO_NOT_FIND = -100; //未找到相关数据
	public static final int ERRNO_EXCEPTION = -101; //服务器内部异常
	public static final int ERRNO_ILLEALDATA = -102; //非法数据

    public ApiJsonResult() {
    	this.put("errno",ERRNO_OK).put("status",STATUS.OK.name().toLowerCase());
    }
    
    public ApiJsonResult(int errno, String error) {
    	this.put("errno",errno).put("error",error).put("status",STATUS.FAIL.name().toLowerCase());
    }

    public ApiJsonResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public ApiJsonResult put(Serializable entity) {
        super.put(StringUtil.uncapitalize(entity.getClass().getSimpleName()), entity);
        return this;
    }
    
    public ApiJsonResult putData(Map<String,Object> data) {
    	return put("data", data);
    }

    public String toJson()  {
    	return JSON.toJSONString(this);
    }

    public ApiJsonResult clone() {
        return (ApiJsonResult) super.clone();
    }

    public String toString()  {
        return toJson();
    }
    
    
}
