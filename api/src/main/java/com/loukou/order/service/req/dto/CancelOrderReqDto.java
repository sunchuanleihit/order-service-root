package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class CancelOrderReqDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2172332893738689864L;
	private int userId = 0;
	private String orderSnMain = "";
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	
}
