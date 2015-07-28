package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayOrderResultRespDto extends ResponseCodeDto implements Serializable {
	
	/**
	 * 
	 */
	
	public PayOrderResultRespDto(int code, String desc) {
		super(code, desc);
	}
	private static final long serialVersionUID = -1039422672447327463L;
	private PayOrderMsgDto result;

	public PayOrderMsgDto getResult() {
		return result;
	}
	public void setResult(PayOrderMsgDto result) {
		this.result = result;
	}
	
}
