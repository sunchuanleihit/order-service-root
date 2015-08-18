package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class ActivateCouponReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 515391985030940343L;

	private int userId = 0;
	
	private String commoncode = "";// 公有券券码
	
	private String openId = "";

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCommoncode() {
		return commoncode;
	}

	public void setCommoncode(String commoncode) {
		this.commoncode = commoncode;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	
}
