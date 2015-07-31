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
	private PayOrderMsgRespDto result = new PayOrderMsgRespDto();

	public PayOrderMsgRespDto getResult() {
		return result;
	}
	public void setResult(PayOrderMsgRespDto result) {
		this.result = result;
	}
	
}
