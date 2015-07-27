package com.loukou.order.service.resp.dto;

public class ShippingMsgRespDto extends ResponseCodeDto{

	public ShippingMsgRespDto(int code, String message) {
		super(code, message);
	}

	private ShippingListResultDto result = new ShippingListResultDto();

	public ShippingListResultDto getResult() {
		return result;
	}

	public void setResult(ShippingListResultDto result) {
		this.result = result;
	}

}
