package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkVaccountRespDto implements Serializable{
	private static final long serialVersionUID = -5607158961228755791L;
	private String orderSnMain;
	private String buyerName;
	private Double inAmount;
	private Double outAmount;
	private String addTime;
	private String note;
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public Double getInAmount() {
		return inAmount;
	}
	public void setInAmount(Double inAmount) {
		this.inAmount = inAmount;
	}
	public Double getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(Double outAmount) {
		this.outAmount = outAmount;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
}
