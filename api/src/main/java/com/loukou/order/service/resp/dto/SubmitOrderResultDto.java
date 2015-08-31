package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class SubmitOrderResultDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3510924009806305928L;
	
	private String orderSnMain = "";
	private double needPay = 0.0;
	
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public double getNeedPay() {
		return needPay;
	}
	public void setNeedPay(double needPay) {
		this.needPay = needPay;
	}
	
	
}
