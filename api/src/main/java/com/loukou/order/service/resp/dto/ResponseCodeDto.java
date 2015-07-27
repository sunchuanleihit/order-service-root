package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ResponseCodeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -11001300903411737L;
	private int code = 200;
	private String message = "";
	
	public ResponseCodeDto(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
