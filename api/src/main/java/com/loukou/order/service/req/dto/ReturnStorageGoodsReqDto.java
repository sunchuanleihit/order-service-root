package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class ReturnStorageGoodsReqDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2740531537561879359L;

	private int specId;
	
	private int quantity;

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
