package com.loukou.order.service.bo;

import java.util.ArrayList;
import java.util.List;

public class ReturnOrderBo {

	private int orderId;//订单Id
	private String postScript;//备注
	private String orderSnMain;//主订单号
	private int returnType;//退款类型：0退货订单1拒收订单2多付款退款 3退运费 4.客户赔偿 5其他退款,6客户自己取消订单退款  7:特殊退款
	private double shippingFee;//运费金额
	private int payId;//支付方式

	private List<ReturnOrderBo> purchaseDetailBo = new ArrayList<ReturnOrderBo>();

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getPostScript() {
		return postScript;
	}

	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public List<ReturnOrderBo> getPurchaseDetailBo() {
		return purchaseDetailBo;
	}

	public void setPurchaseDetailBo(List<ReturnOrderBo> purchaseDetailBo) {
		this.purchaseDetailBo = purchaseDetailBo;
	}
}
