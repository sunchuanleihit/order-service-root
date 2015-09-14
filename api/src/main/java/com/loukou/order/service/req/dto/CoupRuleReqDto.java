package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class CoupRuleReqDto implements Serializable{
	private static final long serialVersionUID = -1040416399570592856L;
	private Integer typeId;
	private Integer ruleId;
	private String ruleName;
	private Integer ruleType;
	private Integer scope;
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	public Integer getScope() {
		return scope;
	}
	public void setScope(Integer scope) {
		this.scope = scope;
	}
	
}
