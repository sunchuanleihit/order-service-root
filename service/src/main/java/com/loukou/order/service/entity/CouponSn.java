package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_coupon_sn")
public class CouponSn {
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "coupon_id")
	private int couponId;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "begintime")
	private Date beginTime;
	
	@Column(name = "endtime")
	private Date endTime;
	
	@Column(name = "commoncode")
	private String commonCode;
	
	@Column(name = "money")
	private double money;
	
	@Column(name = "minprice")
	private double minPrice;
	
	@Column(name = "issue")
	private int issue;//是否启用0：不启用1：为启用2:停用
	
	@Column(name = "ischecked")
	private int isChecked;//是否已被使用,0 未使用，1已使用
	
	@Column(name = "createtime")
	private String createTime;
	
	@Column(name = "usedtime")
	private Date usedTime;
	
	@Column(name = "source")
	private int source;//1.会员后台 2.购物车

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCommonCode() {
		return commonCode;
	}

	public void setCommonCode(String commonCode) {
		this.commonCode = commonCode;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public int getIssue() {
		return issue;
	}

	public void setIssue(int issue) {
		this.issue = issue;
	}

	public int getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

}
