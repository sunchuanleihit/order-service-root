package com.loukou.order.service.resp.dto;

public class ReturnStorageRespDto {
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
