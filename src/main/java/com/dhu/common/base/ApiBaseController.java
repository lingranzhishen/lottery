package com.dhu.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONPObject;
import com.dhu.common.ApiJsonResult;

public abstract class ApiBaseController {
	protected Logger log = LoggerFactory.getLogger(getClass());
	protected final static String EMPTY_STR = "";

	// ok
	public ApiJsonResult ok() {
		return new ApiJsonResult();
	}

	public static ApiJsonResult ok(String key, Object value) {
		return new ApiJsonResult().put(key, value);
	}

	// fail
	public static ApiJsonResult fail(int errno) {
		return fail(errno, EMPTY_STR);
	}

	public static ApiJsonResult fail(int errno, String error) {
		return new ApiJsonResult(errno, error);
	}

	public Object getCallBackResult(ApiJsonResult result, String callback) {
		if (callback == null) {
			return result;
		} else {
			JSONPObject object = new JSONPObject(callback);
			object.addParameter(result);
			return object;
		}
	}
	
}
