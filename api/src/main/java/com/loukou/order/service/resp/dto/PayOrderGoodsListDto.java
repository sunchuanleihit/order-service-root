package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayOrderGoodsListDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2285762359278825772L;
	private String goodsName = "";
	private double price = 0;
	private int amount = 0;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
