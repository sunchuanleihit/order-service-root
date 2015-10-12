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
	private String starttime = "";
	private String endtime = "";
	private String couponMsg = "";
	private int id = 0;
	private String userName = "";
	private String userId = "" ;
	private double minprice = 0.0;//最小金额
	private String createtime = "";
	private String usedtime = "";
	private String canuse = "";//是否启用
	private String ischecked ;
	private String couponRange = "";//品类券的使用范围说明
	private int isUsable = 0;//是否可用（0: 不限——过期和不过期的优惠券；1：可用优惠券）
	private String title;//优惠券所属分类
	
	public String getIschecked() {
		return ischecked;
	}

	public void setIschecked(String ischecked) {
		this.ischecked = ischecked;
	}

	public String getCanuse() {
		return canuse;
	}

	public void setCanuse(String canuse) {
		this.canuse = canuse;
	}

	public String getUsedtime() {
		return usedtime;
	}

	public void setUsedtime(String usedtime) {
		this.usedtime = usedtime;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getMinprice() {
		return minprice;
	}

	public void setMinprice(double minprice) {
		this.minprice = minprice;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

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

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
