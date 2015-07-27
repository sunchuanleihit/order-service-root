package com.loukou.order.service.req.dto;

public class ReturnStorageReqDto {
	private String taoOrderSn;
	private int storeId;
	private ReturnStorageGoodsListReqDto[] goodsList;
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
	public ReturnStorageGoodsListReqDto[] getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(ReturnStorageGoodsListReqDto[] goodsList) {
		this.goodsList = goodsList;
	}
	
	
}
