package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_extm")
public class OrderExtm {
	
	@Id
	@Column(name = "id")
	private int id;

	/*
	 * 子订单号
	 */
	@Column(name = "order_id")
	private Integer orderId = 0;

	/*
	 * 主单号
	 * 如141107091685349
	 */
	@Column(name = "order_sn_main")
	private String orderSnMain = "";

	/*
	 * 称呼
	 */
	@Column(name = "consignee")
	private String consignee = "";
	
	@Column(name = "region_id")
	private Integer regionId = 0;
	
	/*
	 * 对cvs=null
	 */
	@Column(name = "region_id_old")
	private Integer regionIdOld = 0;
	
	@Column(name = "region_name")
	private String regionName = "";
	
	@Column(name = "address")
	private String address = "";

	@Column(name = "zipcode")
	private String zipcode = "";

	@Column(name = "phone_tel")
	private String phoneTel = "";

	@Column(name = "phone_mob")
	private String phoneMob = "";

	@Column(name = "timestamp")
	private Date timestamp = new Date();

	/*
	 * 1删除 0未删除
	 */
	@Column(name = "del_flag")
	private int delFlag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Integer getRegionIdOld() {
		return regionIdOld;
	}

	public void setRegionIdOld(int regionIdOld) {
		this.regionIdOld = regionIdOld;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhoneTel() {
		return phoneTel;
	}

	public void setPhoneTel(String phoneTel) {
		this.phoneTel = phoneTel;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
}
