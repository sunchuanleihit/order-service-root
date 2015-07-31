package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ShippingMsgDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507130177792278342L;
	private String description = "";
	private String creatTime = "";

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

    @Override
    public String toString() {
        return "ShippingMsgDto [description=" + description + ", creatTime=" + creatTime + "]";
    }

}
