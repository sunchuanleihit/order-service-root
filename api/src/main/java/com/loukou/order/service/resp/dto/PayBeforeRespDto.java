package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayBeforeRespDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1532436937246762312L;
	private int code = 200;
	private String message = "";
	private PayBeforeResultDto result = new PayBeforeResultDto();
	
	public PayBeforeRespDto (int code, String message) {
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

	public PayBeforeResultDto getResult() {
		return result;
	}

	public void setResult(PayBeforeResultDto result) {
		this.result = result;
	}
	
}
