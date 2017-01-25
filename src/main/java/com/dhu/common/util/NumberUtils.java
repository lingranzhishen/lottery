package com.dhu.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtils {

	public static double roundDouble(double number,int scale){
		BigDecimal b = new BigDecimal(number);  
		return b.setScale(scale,BigDecimal.ROUND_HALF_EVEN).doubleValue(); 
	}
	
	/**
	 * 保留小数位 四舍五入
	 * @param number
	 * @param digits
	 * @return
	 */
	public static String format(Double number,int scale){
		if(scale < 0) scale = 0;
		String form;
		switch (scale) {
			case 0:  form="#"; break;
			case 1:  form="#.0"; break;
			case 2:  form="#.00"; break;
			case 3:  form="#.000"; break;
			case 4:  form="#.0000"; break;
			case 5:  form="#.00000"; break;
			default: form="#.000000"; break;
		}
		return new DecimalFormat(form).format(number);
	}
	/**
	 * 保留小数位和 format的区别是当小数位0时去掉0
	 * @param number
	 * @param digits
	 * @return
	 */
	public static String round(Double number,int scale){
		if(scale < 0) scale = 0;
		String form;
		switch (scale) {
			case 0:  form="#"; break;
			case 1:  form="#.#"; break;
			case 2:  form="#.##"; break;
			case 3:  form="#.###"; break;
			case 4:  form="#.####"; break;
			case 5:  form="#.#####"; break;
			default: form="#.######"; break;
		}
		return new DecimalFormat(form).format(number);
	}
	
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的差
	 */
	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的积
	 */
	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位
	 * ，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
	 * @param v1
	 * @param v2
	 * @return 两个参数的商
	 */
	public static double divide(double v1, double v2) {
		return divide(v1, v2, 8);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度
	 * ，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
	 * @param v1
	 * @param v2
	 * @param scale 表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double divide(double v1, double v2, int scale) {
		if (scale < 0) {
			scale = 0;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}

	public static void main(String[] args) {
		Double d = 3.0012;
		System.out.println(round(d, 2));
	}
}
