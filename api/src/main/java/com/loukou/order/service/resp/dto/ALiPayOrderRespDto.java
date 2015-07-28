package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ALiPayOrderRespDto extends AbstractPayOrderRespDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -22286338848377267L;
	private ALiPayOrderResultDto result;
	public ALiPayOrderResultDto getResult() {
		return result;
	}
	public void setResult(ALiPayOrderResultDto result) {
		this.result = result;
	}
	
	
}
