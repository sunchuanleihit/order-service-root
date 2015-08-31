package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayOrderMsgDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8615285017922131008L;
	private String orderSnMain;
	private double total;
	private double orderTotal;
	private double shippingFee;
	private double discountAmount;
	private double txkNum;
	private double vcount;
	private String postscript;
	
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}
	public double getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public double getTxkNum() {
		return txkNum;
	}
	public void setTxkNum(double txkNum) {
		this.txkNum = txkNum;
	}
	public double getVcount() {
		return vcount;
	}
	public void setVcount(double vcount) {
		this.vcount = vcount;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
}
