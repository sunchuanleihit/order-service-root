package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_coup_type")
public class CoupType {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;// 优惠券类型名称

	@Column(name = "description")
	private String description;// 优惠券类型描述

	@Column(name = "typeid")
	private int typeid = 0;// 优惠券类型。1:私有券，2:公有券，3:折扣券

	@Column(name = "usenum")
	private int usenum = 0;// 该类型下的优惠券可以互斥使用的次数。0不限使用次数

	@Column(name = "newuser")
	private int newuser;// 0：不限，1:新会员，2，老会员

	@Column(name = "status")
	private int status;// 0:显示，1:逻辑删除

	@Column(name = "sell_site")
	private String sellSite;// 所属城市

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public int getUsenum() {
		return usenum;
	}

	public void setUsenum(int usenum) {
		this.usenum = usenum;
	}

	public int getNewuser() {
		return newuser;
	}

	public void setNewuser(int newuser) {
		this.newuser = newuser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSellSite() {
		return sellSite;
	}

	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}

}
