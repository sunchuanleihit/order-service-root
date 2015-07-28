package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayBeforeRespDto extends ResponseCodeDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1532436937246762312L;
	private PayBeforeResultDto result = new PayBeforeResultDto();
	
	public PayBeforeRespDto (int code, String message) {
		super(code, message);
	}
	

	public PayBeforeResultDto getResult() {
		return result;
	}

	public void setResult(PayBeforeResultDto result) {
		this.result = result;
	}
	
}
