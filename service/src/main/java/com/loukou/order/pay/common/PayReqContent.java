package com.loukou.order.pay.common;

import java.util.ArrayList;
import java.util.List;

import com.loukou.order.service.impl.OrderModels;

public class PayReqContent {

	private int userId;

	private String orderSnMain;

	private double needToPay;

	private List<OrderModels> allModels = new ArrayList<OrderModels>();

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

	public double getNeedToPay() {
		return needToPay;
	}

	public void setNeedToPay(double needToPay) {
		this.needToPay = needToPay;
	}

	public List<OrderModels> getAllModels() {
		return allModels;
	}

	public void setAllModels(List<OrderModels> allModels) {
		this.allModels = allModels;
	}

}
