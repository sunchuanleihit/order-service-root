package com.loukou.order.service.resp.dto.basic;

import java.io.Serializable;

public class PureRespDto implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3433733459889709123L;
	private int code;
	private String errorMsg;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
