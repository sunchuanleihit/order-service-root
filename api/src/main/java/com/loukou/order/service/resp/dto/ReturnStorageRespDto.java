package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ReturnStorageRespDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5183315592730518334L;
	private int code = 200;
	private String errorMsg="";
	private ReturnStorageResultDto result;
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
	public ReturnStorageResultDto getResult() {
		return result;
	}
	public void setResult(ReturnStorageResultDto result) {
		this.result = result;
	}
	
	public ReturnStorageRespDto(int code, String errorMsg) {
		this.code = code;
		this.errorMsg = errorMsg;
	}
	
	public ReturnStorageRespDto(String message) {
		this.result = new ReturnStorageResultDto(message);
	}
	
	public ReturnStorageRespDto() {
		this.result = new ReturnStorageResultDto();
	}
}
