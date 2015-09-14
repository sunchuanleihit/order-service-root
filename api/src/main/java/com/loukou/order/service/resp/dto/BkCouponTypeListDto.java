package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkCouponTypeListDto implements Serializable{

	private static final long serialVersionUID = -4382223828616281607L;
	private Integer id;
	private String title;
	private String description;
	private String type;
	private Integer usenum;
	private Integer newuser;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getSell_site() {
		return sell_site;
	}
	public void setSell_site(String sell_site) {
		this.sell_site = sell_site;
	}
	private Integer status;
	private String sell_site;
}
