package com.dhu.framework.jobmanage.dto;

import java.io.Serializable;

/**
 * 接口用配置信息
 * @author guofei
 *
 */
public class ResultDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5482576788453409565L;

	/**
	 * 状态 成功/失败
	 * true成功 false失败
	 */
	private boolean success = true;
	
	/**
	 * 数据状态控制
	 */
	private int status = 0;
	/**
	 * 由服务端生成的唯一标示
	 */
	private String clientId;
	
	public ResultDTO(){
	}
	
	public ResultDTO(boolean success) {
		this.success = success;
	}
	
	public ResultDTO(boolean success,String clientId) {
		this.success = success;
		this.clientId = clientId;
	}
	
	public ResultDTO(boolean success,String clientId,int status) {
		this.success = success;
		this.clientId = clientId;
		this.status = status;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
