package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class OrderCancelRespDto extends ResponseCodeDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6722881887389992746L;

	public OrderCancelRespDto(int code, String desc) {
		super(code, desc);
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
