package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkOrderRemarkDto implements Serializable{
	private static final long serialVersionUID = -852561958446028332L;
	
	private Integer id;
	private String orderSnMain;
	private String user;
	private String time;
	private String content;
	private Integer closed;
	private Integer type;
	private String userClosed;
	private String closedTime;
	private String finishedTime;
	public String getUserClosed() {
		return userClosed;
	}
	public void setUserClosed(String userClosed) {
		this.userClosed = userClosed;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getClosed() {
		return closed;
	}
	public void setClosed(Integer closed) {
		this.closed = closed;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getClosedTime() {
		return closedTime;
	}
	public void setClosedTime(String closedTime) {
		this.closedTime = closedTime;
	}
	public String getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(String finishedTime) {
		this.finishedTime = finishedTime;
	}

}
