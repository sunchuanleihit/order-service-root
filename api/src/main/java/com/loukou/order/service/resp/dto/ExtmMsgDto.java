package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ExtmMsgDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4852348544033722979L;

	private String consignee = "";
	private String phoneMob = "";
	private String address = "";

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    @Override
    public String toString() {
        return "ExtmMsgDto [consignee=" + consignee + ", phone_mob=" + phone_mob + ", address=" + address + "]";
    }

}
