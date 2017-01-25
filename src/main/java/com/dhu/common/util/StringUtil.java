package com.dhu.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 字符串操作的工具类
 */
public class StringUtil extends StringUtils {
	public static final String EMPTY="";
    /**
     * 判断字符串是否为空，并且该字符为"null"也为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str) || str.trim().length() == 0 || str.trim().toLowerCase().equals("null");
    }

    /**
     * 判断字符串不为空，并且该字符不为"null"
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断对象是否为空
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if(obj == null)
            return true;

        return StringUtils.isEmpty(obj.toString()) || obj.toString().trim().toLowerCase().equals("null");
    }

    /**
     * 判断数据是否不为空
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        if(obj == null)
            return false;

        return StringUtils.isNotEmpty(obj.toString()) && !obj.toString().trim().toLowerCase().equals("null");
    }

    /**
     * 如果字符串为空，则直接返回空符串,接受为null的值
     * @param str
     * @return
     */
    public static String defaultEmptyStr(String str) {
        return isEmpty(str) ? "" : str.trim();
    }

    /**
     * 去除字符串首尾的空格
     * @param str
     * @param defaultStr
     * @return
     */
    public static String defaultStr(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str.trim();
    }

    /**
     * 将Object类型转换成String
     * @param obj
     * @return
     */
    public static String defaultEmptyStr(Object obj) {
        if (obj == null)
            return "";
        return defaultStr(obj.toString(), "");
    }

    /**
     * 将Object数据转换成字符串
     * @param obj
     * @param defaultStr
     * @return
     */
    public static String defaultStr(Object obj, String defaultStr) {
        if (obj == null)
            return defaultStr;
        return defaultStr(obj.toString(), defaultStr);
    }

    /**
     * 清理字符串的\n\t\r
     * @param str
     * @return
     */
    public static String clear(Object str) {
        if (isEmpty(str.toString())) return "";

        return str.toString().trim().replaceAll("[\n\t\r ]", "");
    }

    /**
     * 将字符串UTF编码
     * @param str
     * @return
     */
    public static String encodeUTF(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    /**
     * 将字符串转换成Integer类型
     * @param str
     * @param defaultInt
     * @return
     */
    public static int defaultInteger(String str, Integer defaultInt) {
        if(isEmpty(str) && defaultInt == null)
            defaultInt = 0;

        int iRet;
        String ret = isEmpty(str) ? defaultInt + "" : str;
        try {
            iRet = Integer.parseInt(ret);
        } catch (Exception e) {
            return defaultInt;
        }
        return iRet;
    }

    /**
     * 将字符串转换成Double类型
     * @param str
     * @param defaultDouble
     * @return
     */
    public static double defaultDouble(String str, double defaultDouble) {
        double iRet = 0;
        try {
            iRet = Double.parseDouble(str);
        } catch (Exception e) {
            return defaultDouble;
        }
        return iRet;
    }

    /**
     * 把单词首字母转化为小写.
     * <code>
     * Strings.lowerFirst("") = "";
     * Strings.lowerFirst(null) = null;
     * Strings.lowerFirst("DateUtils") == "dateUtils";
     * </code>
     * @param source
     * @return
     */
    public static String lowerFirst(String source){
        if(isEmpty(source)){
           return  source;
        }
        return source.substring(0,1).toLowerCase() + source.substring(1);
    }


    /**
     * sql语句中特殊字符格式化
     * 处理调用Sqlserver分页存储过程 条件传值问题
     * @param sql
     * @return
     */
    public static String formatSql(String sql){
        if(isEmpty(sql)){
            return sql;
        }
        return  sql.replaceAll("'","''");
    }

     /**
     * 对字符串根据规则解析，比如有字符串"a,b,c"，分隔符为",",valueCount=3，那么解析结果为String [] result = {"a","b","c"};
     *
     * @param strSource  要解析的字符串
     * @param token      分隔符
     * @param valueCount 需要拆分几个值
     * @return
     */
    public static String[] decodeStr(String strSource, String token, int valueCount) {
        if (StringUtils.isEmpty(strSource))
            return null;

        String[] values = strSource.split(token);
        if (values.length != valueCount) {
            return null;
        }

        return values;
    }

    /**
     * 将数组按指定的连接符拼接
     * @param strs
     * @param separator
     * @return
     */
    public static String splice(List<String> strs, String separator) {
        if(strs == null || strs.size() == 0)
            return "";
        StringBuffer buffer = new StringBuffer();
        for(String str : strs) {
            buffer.append(str).append(separator);
        }
        return buffer.toString();
    }

}
