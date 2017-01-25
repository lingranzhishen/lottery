package com.dhu.common.model;

/**
 * 某数据为空时的异常
 *
 * User: kuang
 * Date: 12-8-25
 * Time: 下午10:11
 */
public class NullException extends Exception {

	private static final long serialVersionUID = -8214682414710542979L;

	public NullException(String message) {
        super(message);
    }
}
