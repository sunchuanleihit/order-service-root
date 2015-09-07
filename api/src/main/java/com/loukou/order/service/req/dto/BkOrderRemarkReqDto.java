package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class BkOrderRemarkReqDto implements Serializable{
	private static final long serialVersionUID = -4270414248914701092L;
	private String orderSnMain;//订单编号
	private String user;//操作人
	private Integer type;//操作类型
	private Integer bumen;//部门
	private Integer closed;//是否交接
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getBumen() {
		return bumen;
	}
	public void setBumen(Integer bumen) {
		this.bumen = bumen;
	}
	public Integer getClosed() {
		return closed;
	}
	public void setClosed(Integer closed) {
		this.closed = closed;
	}
}
