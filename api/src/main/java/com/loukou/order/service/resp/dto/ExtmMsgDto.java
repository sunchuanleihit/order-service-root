package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ExtmMsgDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4852348544033722979L;

	private String consignee;
	private String phone_mob;
	private String address;

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone_mob() {
		return phone_mob;
	}

	public void setPhone_mob(String phone_mob) {
		this.phone_mob = phone_mob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
