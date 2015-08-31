package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class AbstractPayOrderRespDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -124693353013863937L;
	private int code = 200;
	private String message = "";
	
	
	public AbstractPayOrderRespDto(int code, String message)
	{
		this.code = code;
		this.message = message;
	}
	public AbstractPayOrderRespDto()
	{
		
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
