package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShippingListResultDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4046797300333866394L;
	private String shippingName = "";
	private String payType = "";
	private List<ShippingListDto> shippingList = new ArrayList<ShippingListDto>();

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public List<ShippingListDto> getShippingList() {
		return shippingList;
	}

	public void setShippingList(List<ShippingListDto> shippingList) {
		this.shippingList = shippingList;
	}

}
