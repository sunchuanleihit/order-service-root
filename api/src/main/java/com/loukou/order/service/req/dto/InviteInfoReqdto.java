package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class InviteInfoReqdto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3923386296790932096L;
	//userID
	private int userId;
	//加载条数
	private int pageSize;
	//查询类型
	private String queryType;
	

	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	
	
}
