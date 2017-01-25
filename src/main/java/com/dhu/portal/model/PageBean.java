package com.dhu.portal.model;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {

	public static final Integer DEFAULT_PAGESIZE = 20;// 默认分页大小
	private static final long serialVersionUID = 23777939477442908L;
	private Integer totalCount;
	private Integer pageSize;
	private Integer pageNum;
	private Integer offset;
	private List<T> pageList;

	public PageBean() {

	}

	public PageBean(Integer totalCount, Integer pageSize, Integer pageNum) {
		setTotalCount(totalCount);
		setPageNum(pageNum);
		setPageSize(pageSize);
	}

	public Integer getTotalCount() {
		if (totalCount == null) {
			return 0;
		}
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageSize() {
		if (pageSize == null)
		{
			return DEFAULT_PAGESIZE;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		if (pageNum == null) {
			return 1;
		}
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getOffset() {
		if (getPageNum() > 1 && (getPageNum() - 1) * getPageSize() < getTotalCount()) {
			return (getPageNum() - 1) * getPageSize();
		}
		return 0;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public List<T> getPageList() {
		return pageList;
	}

	public void setPageList(List<T> pageList) {
		this.pageList = pageList;
	}
}
