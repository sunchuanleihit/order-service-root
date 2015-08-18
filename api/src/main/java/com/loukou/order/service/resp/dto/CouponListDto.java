package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class CouponListDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8413159346299672994L;
	private int couponId = 0;
	private String couponName = "";
	private String commoncode = "";
	private double money = 0.0;
	private String endtime = "";
	private String couponMsg = "";
	
	private String couponRange = "";//品类券的使用范围说明
	private int isUsable = 0;//是否可用（0: 不限——过期和不过期的优惠券；1：可用优惠券）
	

	public String getCouponRange() {
		return couponRange;
	}

	public void setCouponRange(String couponRange) {
		this.couponRange = couponRange;
	}

	public int getIsUsable() {
		return isUsable;
	}

	public void setIsUsable(int isUsable) {
		this.isUsable = isUsable;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}


	public String getCommoncode() {
		return commoncode;
	}

	public void setCommoncode(String commoncode) {
		this.commoncode = commoncode;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCouponMsg() {
		return couponMsg;
	}

	public void setCouponMsg(String couponMsg) {
		this.couponMsg = couponMsg;
	}

}
