package com.loukou.order.service.resp.dto;

public class ShippingResultDto {

	private int code;
	private ResultDto result = new ResultDto();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ResultDto getResult() {
		return result;
	}

	public void setResult(ResultDto result) {
		this.result = result;
	}

}
