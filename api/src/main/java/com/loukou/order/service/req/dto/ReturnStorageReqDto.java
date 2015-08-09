package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class ReturnStorageReqDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -782046267292089115L;
	private String taoOrderSn;
	private int storeId;
	private ReturnStorageGoodsReqDto[] specList;
	public String getTaoOrderSn() {
		return taoOrderSn;
	}
	public void setTaoOrderSn(String taoOrderSn) {
		this.taoOrderSn = taoOrderSn;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public ReturnStorageGoodsReqDto[] getSpecList() {
		return specList;
	}
	public void setSpecList(ReturnStorageGoodsReqDto[] specList) {
		this.specList = specList;
	}
}
