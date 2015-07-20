package com.loukou.order.service.req.dto;

public class PayOrderReqContent {
	private double totalPrice;
	private String orderSnMain;
	private double needToPay;
	private int userId;
	private String userName;

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public double getNeedToPay() {
		return needToPay;
	}

	public void setNeedToPay(double needToPay) {
		this.needToPay = needToPay;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
