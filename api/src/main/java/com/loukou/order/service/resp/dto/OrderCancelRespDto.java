package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class OrderCancelRespDto extends ResponseCodeDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6722881887389992746L;
	
	private String result = "";

	
	public OrderCancelRespDto(int code, String message) {
		super(code, message);
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}

	
}
