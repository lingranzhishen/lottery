package com.dhu.common.model;

import java.util.List;

/**
 * 导出execl的表格model
 * @author Fant
 * @date 2014年9月30日 下午12:35:48
 */

public class SheetObject {

	private List<Object> data;//数据，查数据的sql使用LinkedHashMap为resultType才能保证顺序
	private String sheetName;//表格名称
	private Integer headerNum = 1;//表头行数
	
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
