package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayOrderResultRespDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1039422672447327463L;
	private PayOrderMsgDto result;
	private int code = 200;
	public PayOrderMsgDto getResult() {
		return result;
	}
	public void setResult(PayOrderMsgDto result) {
		this.result = result;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
}
