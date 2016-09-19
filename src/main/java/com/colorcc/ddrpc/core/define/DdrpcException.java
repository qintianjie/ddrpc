package com.colorcc.ddrpc.core.define;

/**
 * Exception
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 11:19:35 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class DdrpcException extends RuntimeException {

	public DdrpcException() {
		super();
	}

	public DdrpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DdrpcException(String message, Throwable cause) {
		super(message, cause);
	}

	public DdrpcException(String message) {
		super(message);
	}

	public DdrpcException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113186254539447781L;

}
