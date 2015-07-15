package com.loukou.order.service.resp.dto;

public class WeixinPayOrderResultDto {

	private String partner;
	private String prepayId;// 预支付订单
	private String nonceStr;// 随机串，防重发、
	private String timeStamp;// 时间戳，防重发、
	private String packageStr;// 商家根据财付通文档填写的数据和签名 TODO 原php接口为package
	private String sign;// 商家根据微信开放平台文档对数据做的签名

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

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getPackageStr() {
		return packageStr;
	}

	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
