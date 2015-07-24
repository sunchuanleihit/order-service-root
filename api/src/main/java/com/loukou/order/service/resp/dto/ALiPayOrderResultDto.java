package com.loukou.order.service.resp.dto;

public class ALiPayOrderResultDto {

	private String outTradeNo;// 外部订单号
	private String orderSnMain;
	private double needPay;
	private String notifyUrl;// 回调地址
	private String partner;// 签约支付宝账号
	private String seller;// 合作者身份ID
	private String rsaPrivateKey;// 私钥
	private String rsaPublicKey;// 公钥

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public double getNeedPay() {
		return needPay;
	}

	public void setNeedPay(double needPay) {
		this.needPay = needPay;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getRsaPrivateKey() {
		return rsaPrivateKey;
	}

	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}

	public String getRsaPublicKey() {
		return rsaPublicKey;
	}

	public void setRsaPublicKey(String rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}

}
