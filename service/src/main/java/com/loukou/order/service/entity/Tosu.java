package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_tosu")
public class Tosu {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "user_name")
	private String userName = "";
	
	@Column(name = "mobile")
	private String mobile = "";

	@Column(name = "order_sn_main")
	private String orderSnMain = "";

	@Column(name = "seller_name")
	private String sellerName = "";
	
	@Column(name = "goods_name")
	private String goodsName = "";
	
	@Column(name = "department")
	private String department = "";
	
	@Column(name = "type")
	private Integer type = null;
	
	@Column(name = "dateline1")
	private Integer dateline1 = null;
	
	@Column(name = "dateline2")
	private Integer dateline2 = null;
	
	@Column(name = "status")
	private Integer status = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDateline1() {
		return dateline1;
	}

	public void setDateline1(Integer dateline1) {
		this.dateline1 = dateline1;
	}

	public Integer getDateline2() {
		return dateline2;
	}

	public void setDateline2(Integer dateline2) {
		this.dateline2 = dateline2;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
