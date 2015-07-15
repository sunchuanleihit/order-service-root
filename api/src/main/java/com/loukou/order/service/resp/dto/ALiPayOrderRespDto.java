package com.loukou.order.service.resp.dto;

public class ALiPayOrderRespDto extends AbstractPayOrderRespDto{

	private int code = 200;
	private ALiPayOrderResultDto result;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public ALiPayOrderResultDto getResult() {
		return result;
	}
	public void setResult(ALiPayOrderResultDto result) {
		this.result = result;
	}
	
	
}
