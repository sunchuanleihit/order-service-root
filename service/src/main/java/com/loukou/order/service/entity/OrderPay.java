package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_pay")
public class OrderPay {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	/*
	 * 主单号
	 * 如141107091685349
	 */
	@Column(name = "order_sn_main")
	private String orderSnMain;

	/*
	 * 子订单号
	 */
	@Column(name = "order_id")
	private int orderId;
	
	/*
	 * 支付类型
	 */
	@Column(name = "payment_id")
	private int paymentId;
	
	/*
	 * 支付金额
	 */
	@Column(name = "money")
	private double money;
	
	/*
	 * 支付时间
	 */
	@Column(name = "pay_time")
	private long payTime;
	
	/*
	 * enum('succ','failed','cancel','error','invalid','progress','timeout','ready')
	 */
	@Column(name = "status")
	private String status;
	
	/*
	 * 对cvs=null
	 */
	@Column(name = "mobil")
	private String mobil;

	/*
	 * 对cvs=null
	 */
	@Column(name = "pwd")
	private String pwd;

	/*
	 * 对cvs=null
	 */
	@Column(name = "ORDERID")
	private String mobileOrderId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMobil() {
		return mobil;
	}

	public void setMobil(String mobil) {
		this.mobil = mobil;
	}

	public String getPwd() {
		return pwd;
	}

	public String getMobileOrderId() {
		return mobileOrderId;
	}

	public void setMobileOrderId(String mobileOrderId) {
		this.mobileOrderId = mobileOrderId;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
