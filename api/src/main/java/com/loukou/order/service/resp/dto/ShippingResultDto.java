package com.loukou.order.service.resp.dto;

public class ShippingResultDto {

	private int code;
	private ShippingListResultDto result = new ShippingListResultDto();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ShippingListResultDto getResult() {
		return result;
	}

	public void setResult(ShippingListResultDto result) {
		this.result = result;
	}

}
