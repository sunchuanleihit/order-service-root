package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_lnglat")
public class OrderLnglat {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "order_sn_main")
	private String orderSnMain = "";
	
	@Column(name = "lnglat")
	private String lnglat = "lat:0,lng:0";

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

	public String getLnglat() {
		return lnglat;
	}

	public void setLnglat(String lnglat) {
		this.lnglat = lnglat;
	}
	
	
	
}
