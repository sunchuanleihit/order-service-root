package com.loukou.order.service.resp.dto;

public class BkExtmMsgDto {
	private Integer id;
	private Integer orderId;
	private String orderSnMain;
	private String consignee;
	private String regionId;
	private String regionIdOld;
	private String regionName;
	private String address;
	private String zipCode;
	private String phoneTel;
	private String phoneMob;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionIdOld() {
		return regionIdOld;
	}
	public void setRegionIdOld(String regionIdOld) {
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
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
	

}
