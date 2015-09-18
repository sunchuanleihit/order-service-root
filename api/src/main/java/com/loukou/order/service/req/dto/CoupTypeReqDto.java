package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class CoupTypeReqDto implements Serializable{
	private static final long serialVersionUID = 7909119777004367550L;
	private Integer id;
	private String title;
	private String description;
	private Integer typeid;
	private Integer usenum;
	private Integer newuser;
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getTypeid() {
		return typeid;
	}
	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}
	public Integer getUsenum() {
		return usenum;
	}
	public void setUsenum(Integer usenum) {
		this.usenum = usenum;
	}
	public Integer getNewuser() {
		return newuser;
	}
	public void setNewuser(Integer newuser) {
		this.newuser = newuser;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}
