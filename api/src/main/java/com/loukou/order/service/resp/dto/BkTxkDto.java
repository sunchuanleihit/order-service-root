package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkTxkDto implements Serializable{
	private static final long serialVersionUID = 7436393476784977242L;
	private String id;
	private String cardnum;
	private Double amount;
	private Double residueAmount;
	private String activeTime;
	private String endTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getResidueAmount() {
		return residueAmount;
	}
	public void setResidueAmount(Double residueAmount) {
		this.residueAmount = residueAmount;
	}
	public String getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
