package com.dhu.framework.jobmanage.dto;

import java.io.Serializable;

public class TaskDispatchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -587048833661430211L;

	public String projectCode;
	
	private String env;
	
	private String className;
	
	private String methodName;
	
	private int lockTimes = 0;
	
	private String clientId;
	
	private int status = 0;

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getLockTimes() {
		return lockTimes;
	}

	public void setLockTimes(int lockTimes) {
		this.lockTimes = lockTimes;
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

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(env).append(".")
			.append(projectCode).append(".")
			.append(className).append(".")
			.append(methodName);
		return sb.toString();
	}
	
}
