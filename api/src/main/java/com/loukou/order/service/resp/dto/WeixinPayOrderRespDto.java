package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class WeixinPayOrderRespDto extends AbstractPayOrderRespDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1369856596207408157L;
	private WeixinPayOrderResultDto result;


	public WeixinPayOrderResultDto getResult() {
		return result;
	}

	public void setResult(WeixinPayOrderResultDto result) {
		this.result = result;
	}

}
