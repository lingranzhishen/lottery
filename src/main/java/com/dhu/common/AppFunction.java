package com.dhu.common;



public class AppFunction {
	
	/**
	 * 字符串包含
	 * @param source
	 * @param con
	 * @return
	 */
	public static boolean splitContains(String source,Object con) {
        if(source==null|| con==null) return false;
        if(!source.startsWith(",")){
        	source = ","+source;
        }
        if(!source.endsWith(",")){
        	source = source + ",";
        }
        return (","+source+",").contains(","+con.toString()+",");
    }
	
	/**
	 * 检测当前用户是否有权限（权限区分公司）
	 * @param priUrl
	 * @return
	 */
	public static boolean checkPower(String priUrl) {
		return true;
	}
}
