package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_payment")
public class Payment {

	@Id
	@GeneratedValue
	@Column(name = "payment_id")
	private int paymentId;

	@Column(name = "payment_code")
	private String paymentCode = "";

	@Column(name = "payment_name")
	private String paymentName = "";

	@Column(name = "payment_desc")
	private String paymentDesc = "";

	@Column(name = "config")
	private String config = "";

	@Column(name = "is_online")
	private int isOnline = 0;//

	@Column(name = "enabled")
	private int enabled = 0;//

	@Column(name = "sort_order")
	private int sort_order = 0;

	@Column(name = "payment_id_old")
	private int paymentIdOld = 0;

	@Column(name = "bank_act")
	private int bankAct = 0;// 0不参与银行专享受 1参与

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getPaymentDesc() {
		return paymentDesc;
	}

	public void setPaymentDesc(String paymentDesc) {
		this.paymentDesc = paymentDesc;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getSort_order() {
		return sort_order;
	}

	public void setSort_order(int sort_order) {
		this.sort_order = sort_order;
	}

	public int getPaymentIdOld() {
		return paymentIdOld;
	}

	public void setPaymentIdOld(int paymentIdOld) {
		this.paymentIdOld = paymentIdOld;
	}

	public int getBankAct() {
		return bankAct;
	}

	public void setBankAct(int bankAct) {
		this.bankAct = bankAct;
	}

}
