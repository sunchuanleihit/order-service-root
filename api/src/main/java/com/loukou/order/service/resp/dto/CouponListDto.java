package com.loukou.order.service.resp.dto;

public class CouponListDto {
	private int couponId;
	private String couponName;
	private String commoncode;
	private String money;
	private String endtime;
	private String couponMsg;

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

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
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
