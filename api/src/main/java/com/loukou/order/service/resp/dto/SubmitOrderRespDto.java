package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class SubmitOrderRespDto extends ResponseCodeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2898285376743994421L;
	
	public SubmitOrderRespDto(int code, String message) {
		super(code, message);
	}
	
	private SubmitOrderResultDto result = new SubmitOrderResultDto();

	public SubmitOrderResultDto getResult() {
		return result;
	}
	public void setResult(SubmitOrderResultDto result) {
		this.result = result;
	}
	
	
}
