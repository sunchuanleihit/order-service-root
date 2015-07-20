package com.loukou.order.service.resp.dto;

public class ResponseCodeDto {
	private int code = 200;
	private String desc = "";
	
	public ResponseCodeDto(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
