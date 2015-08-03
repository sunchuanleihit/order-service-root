package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class ReturnStorageGoodsReqDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2740531537561879359L;

	private int specId;
	
	private int confirmNum;

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public int getConfirmNum() {
		return confirmNum;
	}

	public void setConfirmNum(int confirmNum) {
		this.confirmNum = confirmNum;
	}
}
