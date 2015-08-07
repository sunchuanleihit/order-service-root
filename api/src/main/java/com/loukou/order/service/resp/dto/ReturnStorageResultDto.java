package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ReturnStorageResultDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -677825744191017373L;
	private String message="";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReturnStorageResultDto(String message) {
		this.message = message;
	}

	public ReturnStorageResultDto() {
	}
}
