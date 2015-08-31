package com.loukou.order.service.resp.dto;

import java.io.Serializable;


public class ResponseDto <T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1557028649389397553L;


	public ResponseDto (int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	private int code = 200;
	private String message = "";
	private T result = null;
	
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
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	
}
