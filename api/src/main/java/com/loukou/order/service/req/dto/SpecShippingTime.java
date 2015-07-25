package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class SpecShippingTime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4289108451992986790L;
	private int specId = 0;
	private String time = "";
	
	public int getSpecId() {
		return specId;
	}
	public void setSpecId(int specId) {
		this.specId = specId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
