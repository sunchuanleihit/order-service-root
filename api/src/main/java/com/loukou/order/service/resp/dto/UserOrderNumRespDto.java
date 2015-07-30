package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class UserOrderNumRespDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -382158094870622521L;

	private int payNum = 0;
	
	private int deliveryNum = 0;
	
	private int refundNum = 0;

	public int getPayNum() {
		return payNum;
	}

	public void setPayNum(int payNum) {
		this.payNum = payNum;
	}

	public int getDeliveryNum() {
		return deliveryNum;
	}

	public void setDeliveryNum(int deliveryNum) {
		this.deliveryNum = deliveryNum;
	}

	public int getRefundNum() {
		return refundNum;
	}

	public void setRefundNum(int refundNum) {
		this.refundNum = refundNum;
	}

}
