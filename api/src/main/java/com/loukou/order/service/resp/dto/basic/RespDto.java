package com.loukou.order.service.resp.dto.basic;

import java.io.Serializable;

public class RespDto<T> implements Serializable {

	private static final long serialVersionUID = -7961550195550167930L;
	
	private int code;
	private String errorMsg;
	private T result;
	
	public RespDto(int code, String msg, T result) {
		this.code = code;
		this.errorMsg = msg;
		this.result = result;
	}
	
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
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	
	
	
}
