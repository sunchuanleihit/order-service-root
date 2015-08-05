package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_pay_r")
public class OrderPayR {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "order_id_r")
	private int orderIdR = 0;// 反向订单号

	@Column(name = "repay_way")
	private int repayWay = 0;// 0:虚拟账户；1：原路返回

	@Column(name = "payment_id")
	private int paymentId = 0;// 付款类型

	@Column(name = "value")
	private double value = 0.0;// 退款金额

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderIdR() {
		return orderIdR;
	}

	public void setOrderIdR(int orderIdR) {
		this.orderIdR = orderIdR;
	}

	public int getRepayWay() {
		return repayWay;
	}

	public void setRepayWay(int repayWay) {
		this.repayWay = repayWay;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
