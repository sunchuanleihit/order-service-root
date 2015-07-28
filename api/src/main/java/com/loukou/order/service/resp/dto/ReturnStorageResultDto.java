package com.loukou.order.service.resp.dto;

public class ReturnStorageResultDto {
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
