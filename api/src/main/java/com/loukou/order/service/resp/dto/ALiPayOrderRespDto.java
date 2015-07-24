package com.loukou.order.service.resp.dto;

public class ALiPayOrderRespDto extends AbstractPayOrderRespDto{

	private ALiPayOrderResultDto result;
	public ALiPayOrderResultDto getResult() {
		return result;
	}
	public void setResult(ALiPayOrderResultDto result) {
		this.result = result;
	}
	
	
}
