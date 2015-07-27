package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ShippingMsgRespDto extends ResponseCodeDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2121971854328859199L;

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
