package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class CoupRuleDto implements Serializable{
	private static final long serialVersionUID = 5021579054917567334L;
	private Integer id;
	private String name;
	private String coupTypeName;//类别名称
	private String commoncode;//公有券券码
	private String isuse;//启用
	private Integer maxnum;//最大发放量
	private String prefix;//券码前缀
	private Double money;//券码金额
	private Double lowemoney;//最低消费金额
	private Double returnMoney;//返利金额
	private String type;//券码类型
	private String scope;//使用范围
	private String canuseday;//有效期
	private String outId;
	private String categoryName;
	
	public String getOutId() {
		return outId;
	}
	public void setOutId(String outId) {
		this.outId = outId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCommoncode() {
		return commoncode;
	}
	public void setCommoncode(String commoncode) {
		this.commoncode = commoncode;
	}
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
	public Integer getMaxnum() {
		return maxnum;
	}
	public void setMaxnum(Integer maxnum) {
		this.maxnum = maxnum;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getLowemoney() {
		return lowemoney;
	}
	public void setLowemoney(Double lowemoney) {
		this.lowemoney = lowemoney;
	}
	public Double getReturnMoney() {
		return returnMoney;
	}
	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}
	public String getCoupTypeName() {
		return coupTypeName;
	}
	public void setCoupTypeName(String coupTypeName) {
		this.coupTypeName = coupTypeName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getCanuseday() {
		return canuseday;
	}
	public void setCanuseday(String canuseday) {
		this.canuseday = canuseday;
	}
	

}
