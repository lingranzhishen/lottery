package com.dhu.portal.model;

import java.io.Serializable;

/**
 * 会话管理的用户
 */
public class UserInfo implements Serializable {
	
	private static final long serialVersionUID = -871597773301755318L;
	private long id;
	private String displayName;
	private String mobile;
	private String token;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
