package com.loukou.order.service.resp.dto;

public class WeixinPayOrderRespDto extends AbstractPayOrderRespDto{

	private WeixinPayOrderResultDto result;


	public WeixinPayOrderResultDto getResult() {
		return result;
	}

	public void setResult(WeixinPayOrderResultDto result) {
		this.result = result;
	}

}
