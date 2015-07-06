package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class OrderListRespDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2646527710985526663L;
	private int code = 200;
	private OrderListResultDto result;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public OrderListResultDto getResult() {
		return result;
	}
	public void setResult(OrderListResultDto result) {
		this.result = result;
	}
	
	
	
}
