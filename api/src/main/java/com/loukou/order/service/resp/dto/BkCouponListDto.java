package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkCouponListDto implements Serializable{

	private static final long serialVersionUID = -4382223828616281607L;
	private String commonCode;
	private Double money;
	private String couponName;
	private Integer couponTypeId;
	private String couponTypeName;
	private Integer couponFormId;
	private String couponFormName;
	private String orderSnMain;
	private Integer isSue;
	private Integer isChecked;
	private String usedTime;
	private String createTime;
	private String endTime;
	
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public String getCommonCode() {
		return commonCode;
	}
	public void setCommonCode(String commonCode) {
		this.commonCode = commonCode;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public Integer getCouponTypeId() {
		return couponTypeId;
	}
	public void setCouponTypeId(Integer couponTypeId) {
		this.couponTypeId = couponTypeId;
	}
	public String getCouponTypeName() {
		return couponTypeName;
	}
	public void setCouponTypeName(String couponTypeName) {
		this.couponTypeName = couponTypeName;
	}
	public Integer getCouponFormId() {
		return couponFormId;
	}
	public void setCouponFormId(Integer couponFormId) {
		this.couponFormId = couponFormId;
	}
	public String getCouponFormName() {
		return couponFormName;
	}
	public void setCouponFormName(String couponFormName) {
		this.couponFormName = couponFormName;
	}
	public Integer getIsSue() {
		return isSue;
	}
	public void setIsSue(Integer isSue) {
		this.isSue = isSue;
	}
	public Integer getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Integer isChecked) {
		this.isChecked = isChecked;
	}
	public String getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	

}
