package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_tczcount_recharge")
public class TczcountRecharge {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "order_sn_main")
	private String orderSnMain = "";

	@Column(name = "money")
	private Double money = 0.0;
	
	@Column(name = "return_money")
	private Double returnMoney = 0.0;

	@Column(name = "a_id")
	private Integer a_id = 0;
	
	@Column(name = "pay_id")
	private Integer payId = 0;

	@Column(name = "status")
	private Integer status = 0;
	
	@Column(name = "ctime")
	private Integer ctime = 0;

	@Column(name = "ftime")
	private Integer ftime = 0;

	@Column(name = "invoice_no")
	private String invoiceNo = "";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}

	public Integer getA_id() {
		return a_id;
	}

	public void setA_id(Integer a_id) {
		this.a_id = a_id;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getCtime() {
		return ctime;
	}

	public void setCtime(Integer ctime) {
		this.ctime = ctime;
	}

	public Integer getFtime() {
		return ftime;
	}

	public void setFtime(Integer ftime) {
		this.ftime = ftime;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
}
