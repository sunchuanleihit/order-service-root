package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_remark")
public class OrderRemark {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name="user")
	private String user;
	
	@Column(name="time")
	private Date time;
	
	@Column(name="content")
	private String content;
	
	@Column(name="order_sn_main")
	private String orderSnMain;
	
	@Column(name="type")
	private int type;
	
	@Column(name="closed")
	private int closed;
	
	@Column(name="user_closed")
	private String userClosed;
	
	@Column(name="closed_time")
	private Date closedTime;
	
	@Column(name="bumen")
	private int bumen;
	
	@Column(name="finished_time")
	private Date finishedTime;

	public String getUserClosed() {
		return userClosed;
	}

	public void setUserClosed(String userClosed) {
		this.userClosed = userClosed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getClosed() {
		return closed;
	}

	public void setClosed(int closed) {
		this.closed = closed;
	}

	public Date getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Date closedTime) {
		this.closedTime = closedTime;
	}

	public int getBumen() {
		return bumen;
	}

	public void setBumen(int bumen) {
		this.bumen = bumen;
	}

	public Date getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
}
