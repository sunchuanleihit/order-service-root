package com.loukou.order.service.bo;

import java.util.ArrayList;
import java.util.List;

public class ReturnOrderPayBo {
	private int paymentId;//支付Id
	private double paymentAmount;//支付金额

	private List<ReturnOrderPayBo> purchaseDetailBo = new ArrayList<ReturnOrderPayBo>();

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public List<ReturnOrderPayBo> getPurchaseDetailBo() {
		return purchaseDetailBo;
	}

	public void setPurchaseDetailBo(List<ReturnOrderPayBo> purchaseDetailBo) {
		this.purchaseDetailBo = purchaseDetailBo;
	}
}
