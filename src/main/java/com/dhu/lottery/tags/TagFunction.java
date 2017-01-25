package com.dhu.lottery.tags;

import com.dhu.common.Constants;

/**
 * 标签函数
 * 
 * @author Fant
 */
public class TagFunction {

    private final static double PI = 3.14159265358979323; // 圆周率
    private final static double R = 6371229; // 地球的半径

	/**
	 * 生成是否为当前选中项的class
	 * 
	 * @param currentValue
	 * @param selectedValue
	 * @return
	 */
	public static String getSelectedClass(String currentValue, String selectedValue) {
		if (currentValue.equals(selectedValue)) {
			return "on";
		}
		return "";
	}



	/**
	 * 获取一个编码的前缀
	 * 
	 * @return
	 */
	private static String getPrefix(String code) {
		char[] chars = code.toCharArray();
		StringBuffer returnCode = new StringBuffer();
		for (char c : chars) {
			if ((c >= '0' && c <= '9')) {
				break;
			}
			returnCode.append(c);
		}
		return returnCode.toString();
	}

	
	public static void main(String[] args) throws Exception {
		// String res = makeHouseUrl("","t4",2);
		
	}
}
