package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkOrderListRespDto extends ResponseCodeDto implements Serializable {
	
	public BkOrderListRespDto(int code, String message) {
		super(code, message);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2646527710985526663L;
	private BkOrderListResultDto result = new BkOrderListResultDto();
	
	public BkOrderListResultDto getResult() {
		return result;
	}
	public void setResult(BkOrderListResultDto result) {
		this.result = result;
	}
	
	
	
}
