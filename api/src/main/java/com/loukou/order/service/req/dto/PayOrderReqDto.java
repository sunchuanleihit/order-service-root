package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class PayOrderReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3180265881054848033L;
	private int userId;
	private int payType;// 支付类别 1货到付款2在线支付
	private int paymentId;// 支付类别 4支付宝 207微信支付
	private String orderSnMain;
	private int isTaoxinka;// 是否使用淘心卡 1是 0否
	private int isVcount;// 是否使用虚拟账户 1是 0否

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public int getIsTaoxinka() {
		return isTaoxinka;
	}

	public void setIsTaoxinka(int isTaoxinka) {
		this.isTaoxinka = isTaoxinka;
	}

	public int getIsVcount() {
		return isVcount;
	}

	public void setIsVcount(int isVcount) {
		this.isVcount = isVcount;
	}

}
