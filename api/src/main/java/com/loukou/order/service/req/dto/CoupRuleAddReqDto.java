package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class CoupRuleAddReqDto implements Serializable{
	private static final long serialVersionUID = -698480069641951992L;
	private Integer typeId;//规则类别
	private String couponName;//名称
	private Integer maxnum;//最大发放量
	private Integer coupontypeid;//优惠全类型
	private Double lowemoney;//最低消费金额
	private Double money;//优惠金额
	private Integer canuseType;
	private Double returnmoney;//返利金额
	private Integer canuseday;//可用天数
	private String begintime;//开始日期
	private String endtime;//结束时间
	private Integer couponType;//使用范围
	private Integer category;//优惠分类
	private String commoncode;
	private String prefix;
	
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public String getCommoncode() {
		return commoncode;
	}
	public void setCommoncode(String commoncode) {
		this.commoncode = commoncode;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Integer getCanuseType() {
		return canuseType;
	}
	public void setCanuseType(Integer canuseType) {
		this.canuseType = canuseType;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public Integer getMaxnum() {
		return maxnum;
	}
	public void setMaxnum(Integer maxnum) {
		this.maxnum = maxnum;
	}
	public Integer getCoupontypeid() {
		return coupontypeid;
	}
	public void setCoupontypeid(Integer coupontypeid) {
		this.coupontypeid = coupontypeid;
	}
	public Double getLowemoney() {
		return lowemoney;
	}
	public void setLowemoney(Double lowemoney) {
		this.lowemoney = lowemoney;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getReturnmoney() {
		return returnmoney;
	}
	public void setReturnmoney(Double returnmoney) {
		this.returnmoney = returnmoney;
	}
	public Integer getCanuseday() {
		return canuseday;
	}
	public void setCanuseday(Integer canuseday) {
		this.canuseday = canuseday;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public Integer getCouponType() {
		return couponType;
	}
	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}
}
