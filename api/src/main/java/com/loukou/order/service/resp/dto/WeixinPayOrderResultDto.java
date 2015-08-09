package com.loukou.order.service.resp.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeixinPayOrderResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8334750079601385185L;
	private String appId = "";
	private String partner = "";
	private String prepayId = "";// 预支付订单
    @JsonProperty("package")
	private String packageStr = "";// 商家根据财付通文档填写的数据和签名，参数为java关键字package
	private String nonceStr = "";// 随机串，防重发、
	private int timeStamp = 0;// 时间戳，防重发、
	private String sign = ""; // 商家根据微信开放平台文档对数据做的签名
	private double needPay = 0.0; //需要支付的金额
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getPackageStr() {
		return packageStr;
	}
	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	
	public int getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public double getNeedPay() {
		return needPay;
	}
	public void setNeedPay(double needPay) {
		this.needPay = needPay;
	}
}
