package com.loukou.order.service.req.dto;

public class ReturnStorageReqDto {
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
