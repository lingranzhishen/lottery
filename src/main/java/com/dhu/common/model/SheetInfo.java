package com.dhu.common.model;

import java.util.List;

/**
 * 导出execl的表格model
 * @author Fant
 * @date 2014年9月30日 下午12:35:48
 */

public class SheetInfo {

	private Class<?> c;//表格列对应类
	private List<Object> data;//数据
	private String sheetName;//表格名称
	private Integer headerNum = 1;//表头行数
	
	public Class<?> getC() {
		return c;
	}
	public void setC(Class<?> c) {
		this.c = c;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public Integer getHeaderNum() {
		return headerNum;
	}
	public void setHeaderNum(Integer headerNum) {
		this.headerNum = headerNum;
	}
	
	
}
