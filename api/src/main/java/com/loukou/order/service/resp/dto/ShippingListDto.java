package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ShippingListDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5641901360425123642L;
	private String taoOrderSn = "";
	private String description = "";
	private String creatTime = "";

	public String getTaoOrderSn() {
		return taoOrderSn;
	}

	public void setTaoOrderSn(String taoOrderSn) {
		this.taoOrderSn = taoOrderSn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}


}
