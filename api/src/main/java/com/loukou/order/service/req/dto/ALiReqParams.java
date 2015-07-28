package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class ALiReqParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4342590151785846276L;
	/* 基本信息 */
	private String agent;
	private String service;
	private String partner;
	private String inputCharset;
	private String notifyUrl;
	private String returnUrl;
	/* 业务参数 */
	private String subject;
	private String outTradeNo;
	private double price;
	private int quantity;
	private int paymentType;
	/* 物流参数 */
	private String logisticsType = "EXPRESS";
	private double logisticsFee;
	private String logisticsPayment = "BUYER_PAY_AFTER_RECEIVE";
	/* 买卖双方信息 */
	private String sellerId;
	
	private String sign;
	private String signType = "MD5";

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public String getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(String logisticsType) {
		this.logisticsType = logisticsType;
	}

	public double getLogisticsFee() {
		return logisticsFee;
	}

	public void setLogisticsFee(double logisticsFee) {
		this.logisticsFee = logisticsFee;
	}

	public String getLogisticsPayment() {
		return logisticsPayment;
	}

	public void setLogisticsPayment(String logisticsPayment) {
		this.logisticsPayment = logisticsPayment;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

}
