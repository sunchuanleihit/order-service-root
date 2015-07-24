package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class SubmitOrderReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5936322944946180433L;
	private int userId = 0;
	private String openId = "";
	private int storeId = 0;
	private int cityId = 0;
	private int addressId = 0;
	private ShippingTime shippingTimes = new ShippingTime(); 
	private String os = "";	//系统版本 21:android，30：ios
	private int invoiceType = 1;	// 发票类型 1普票 2增票
	private String invoiceHeader = "";	//发票抬头
	private String postScript = "";		// 备注
	private int couponId = 0;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public ShippingTime getShippingTimes() {
		return shippingTimes;
	}
	public void setShippingTimes(ShippingTime shippingTimes) {
		this.shippingTimes = shippingTimes;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public int getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoiceHeader() {
		return invoiceHeader;
	}
	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}
	public String getPostScript() {
		return postScript;
	}
	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	
}
