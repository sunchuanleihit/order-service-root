package com.loukou.order.service.resp.dto;

public class WeixinPayOrderRespDto extends AbstractPayOrderRespDto{

	private int code = 200;
	private WeixinPayOrderResultDto result;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public WeixinPayOrderResultDto getResult() {
		return result;
	}

	public void setResult(WeixinPayOrderResultDto result) {
		this.result = result;
	}

}
