package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkOrderPayDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7774184732882824128L;
	private Integer id;
	private String orderSnMain;
	private Integer orderId;
	private Integer paymentId;
	private String paymentName;
	private double money;
	private long payTime;
	private String status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public long getPayTime() {
		return payTime;
	}
	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaymentName() {
		return paymentName;
	}
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}
}
