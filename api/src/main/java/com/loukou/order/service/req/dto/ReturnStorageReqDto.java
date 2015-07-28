package com.loukou.order.service.req.dto;

public class ReturnStorageReqDto {
	private String taoOrderSn;
	private int storeId;
	private ReturnStorageGoodsReqDto[] goodsList;
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
	public ReturnStorageGoodsReqDto[] getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(ReturnStorageGoodsReqDto[] goodsList) {
		this.goodsList = goodsList;
	}
	
	
}
