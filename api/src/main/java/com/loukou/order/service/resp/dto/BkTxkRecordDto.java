package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkTxkRecordDto implements Serializable{
	private static final long serialVersionUID = 5379623977741413750L;
	
	private String orderCode;
	private String useTime;
	private String cardNum;
	private String useAmount;
	private String currentAmount;
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getUseAmount() {
		return useAmount;
	}
	public void setUseAmount(String useAmount) {
		this.useAmount = useAmount;
	}
	public String getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(String currentAmount) {
		this.currentAmount = currentAmount;
	}
	
	
	
}
