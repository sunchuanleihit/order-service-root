package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class CoupRuleDto implements Serializable{
	private static final long serialVersionUID = 5021579054917567334L;
	private Integer id;
	private String name;
	private String type;
	private String scope;
	private String canuseday;
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
