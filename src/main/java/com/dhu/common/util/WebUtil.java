package com.dhu.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: kuang
 * Date: 12-4-25
 * Time: 下午2:59
 */
public class WebUtil {

	private static final Logger log = LoggerFactory.getLogger(WebUtil.class);
    /** Name suffixes in case of image buttons */
    public static final String[] SUBMIT_IMAGE_SUFFIXES = {".x", ".y"};

    /**
     * 通过名字从 request parameters 中获取值，并返回字符串
     * 如果值为null，则默认返回空字符串
     * @param request
     * @param name
     * @return
     */
    public static String findParamStr(ServletRequest request, String name, String defaultStr) {
        return StringUtil.defaultStr(findParameterValue(request, name), defaultStr);
    }

    public static String findParamStr(ServletRequest request, String name) {
        return findParamStr(request, name, "");
    }

    /**
     * 通过名字从 request parameters 中获取值，并返回 int
     * 如果值为空，默认返回 0
     * @param request
     * @param name
     * @return
     */
    public static int findParamInt(ServletRequest request, String name, int defaultInt) {
        return StringUtil.defaultInteger(findParamStr(request, name), defaultInt);
    }

    public static int findParamInt(ServletRequest request, String name) {
        return findParamInt(request, name, 0);
    }

    /**
     * 通过名字从 request parameters 中获取值，并返回 Integer
     * 如果值为空，默认返回 null
     * @param request
     * @param name
     * @return
     */
    public static Integer findParamInteger(ServletRequest request, String name) {
        String paramValue = findParamStr(request, name);
        if( StringUtil.isEmpty(paramValue)){
            return null;
        }
        try{
            return Integer.parseInt(paramValue);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 通过名字从 request parameters 中获取值，并返回 double
     * 如果值为空，默认返回 0
     * @param request
     * @param name
     * @return
     */
    public static double findParamDoub(ServletRequest request, String name, double defaultDouble) {
        return StringUtil.defaultDouble(findParamStr(request, name), defaultDouble);
    }

    public static double findParamDoub(ServletRequest request, String name) {
        return findParamDoub(request, name, 0);
    }

    /**
     * 通过名字从 request parameters 中获取值，并返回 Double
     * 如果值为空，默认返回 null
     * @param request
     * @param name
     * @return
     */
    public static Double findParamDouble(ServletRequest request, String name) {
        String paramValue = findParamStr(request, name);
        if(StringUtil.isEmpty(paramValue)){
            return null;
        }
        try{
            return Double.parseDouble(paramValue);
        } catch (Exception e) {
        	log.error(e.toString());
            return null;
        }
    }

    /**
     * 通过名字从 request parameters 中获取值，并返回 boolean
     * 如果值为空，默认返回 0
     * @param request
     * @param name
     * @return
     */
    public static boolean findParamBool(ServletRequest request, String name, boolean defaultBoolean) {
        Boolean retBoolean = findParamBoolean(request, name);
        return retBoolean == null ? defaultBoolean : retBoolean;
    }

    public static boolean findParamBool(ServletRequest request, String name) {
        return findParamBool(request, name, false);
    }

    /**
     * 通过名字从 request parameters 中获取值，并返回 Boolean
     * 如果值为空，默认返回 null
     * @param request
     * @param name
     * @return
     */
    public static Boolean findParamBoolean(ServletRequest request, String name) {
        String paramValue = findParamStr(request, name);
        if(StringUtil.isEmpty(paramValue)){
            return  null;
        }
        try{
            return Boolean.parseBoolean(paramValue);
        } catch (Exception e) {
        	log.error(e.toString());
            return null;
        }
    }

    public static Object getSessionAttribute(HttpServletRequest request, String name) {
        Assert.notNull(request, "Request must not be null");
        HttpSession session = request.getSession(false);
        return (session != null ? session.getAttribute(name) : null);
    }

    public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        Assert.notNull(request, "Request must not be null");
        if (value != null) {
            request.getSession().setAttribute(name, value);
        }
        else {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(name);
            }
        }
    }

    @SuppressWarnings("unchecked")
	public static String findParameterValue(ServletRequest request, String name) {
        return findParameterValue(request.getParameterMap(), name);
    }

    public static String findParameterValue(Map<String, ?> parameters, String name) {
        // First try to get it as a normal name=value parameter
        Object value = parameters.get(name);
        if (value instanceof String[]) {
            String[] values = (String[]) value;
            return (values.length > 0 ? values[0] : null);
        }
        else if (value != null) {
            return value.toString();
        }
        // If no value yet, try to get it as a name_value=xyz parameter
        String prefix = name + "_";
        for (String paramName : parameters.keySet()) {
            if (paramName.startsWith(prefix)) {
                // Support images buttons, which would submit parameters as name_value.x=123
                for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
                    if (paramName.endsWith(suffix)) {
                        return paramName.substring(prefix.length(), paramName.length() - suffix.length());
                    }
                }
                return paramName.substring(prefix.length());
            }
        }
        // We couldn't find the parameter value...
        return null;
    }
    
    /**
	 * 添加一个全局cookie
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param domain
	 *            可访问的域名
	 * @param hour
	 *            有效多少小时？0代表当前会话
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value, String domain, int hour) {
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath("/");
		if(hour > 0){
			cookie.setMaxAge(60 * 60 * hour);
		}
		response.addCookie(cookie);
	}

	/**
	 * 读取dookie
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = readCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie.getValue();
		} else {
			return null;
		}
	}

	private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
}
