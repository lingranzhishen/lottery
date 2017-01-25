package com.dhu.common.model;

/**
 * 方法执行时出现的异常
 * 捕获该异常会返回 500状态
 * User: kuang
 * Date: 12-8-25
 * Time: 下午10:09
 */
public class FunctionException extends Exception {

	private static final long serialVersionUID = -2477605969618119663L;

	public FunctionException(String message) {
        super(message);
    }
}
