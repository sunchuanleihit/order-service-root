package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class WeixinPayOrderResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8334750079601385185L;
	private String appId = "";
	private String partnerId = "";
	private String prepayId = "";// 预支付订单
	private String packageX = "";// 商家根据财付通文档填写的数据和签名，参数为java关键字package
	private String nonceStr = "";// 随机串，防重发、
	private String timestamp = "";// 时间戳，防重发、
	private String sign = ""; // 商家根据微信开放平台文档对数据做的签名
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPackageX() {
		return packageX;
	}
	public void setPackageX(String packageX) {
		this.packageX = packageX;
	}
}
