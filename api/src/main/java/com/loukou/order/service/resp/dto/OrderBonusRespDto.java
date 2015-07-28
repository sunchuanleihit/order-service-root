package com.loukou.order.service.resp.dto;


public class OrderBonusRespDto  {

	private String timeStr;
	private int orderNum;
	private double feedback;
	private double feedbackDelivery;
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public double getFeedback() {
		return feedback;
	}
	public void setFeedback(double feedback) {
		this.feedback = feedback;
	}
	public double getFeedbackDelivery() {
		return feedbackDelivery;
	}
	public void setFeedbackDelivery(double feedbackDelivery) {
		this.feedbackDelivery = feedbackDelivery;
	}

}
