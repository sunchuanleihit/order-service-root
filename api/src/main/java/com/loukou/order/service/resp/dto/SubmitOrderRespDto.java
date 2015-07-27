package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class SubmitOrderRespDto extends ResponseCodeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2898285376743994421L;
//	private int code = 200;
//	private String message = "";
	
	public SubmitOrderRespDto(int code, String message) {
		super(code, message);
	}
	
	private SubmitOrderResultDto result = new SubmitOrderResultDto();
//	public int getCode() {
//		return code;
//	}
//	public void setCode(int code) {
//		this.code = code;
//	}
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
	public SubmitOrderResultDto getResult() {
		return result;
	}
	public void setResult(SubmitOrderResultDto result) {
		this.result = result;
	}
	
	
}
