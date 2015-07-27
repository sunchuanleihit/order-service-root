package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class OrderListRespDto extends ResponseCodeDto implements Serializable {
	
	public OrderListRespDto(int code, String desc) {
		super(code, desc);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2646527710985526663L;
	private OrderListResultDto result;
	
	public OrderListResultDto getResult() {
		return result;
	}
	public void setResult(OrderListResultDto result) {
		this.result = result;
	}
	
	
	
}
