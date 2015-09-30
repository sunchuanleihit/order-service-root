package com.loukou.order.service.bo;

import java.util.ArrayList;
import java.util.List;

public class ReturnOrderGoodsBo {

	private int goodsId;//商品Id
	private int specId;//规格Id
	private int proType;//商品类型 ：1普通 2特价
	private int recId;//order_goods表主键
	private int goodsNum;//退货商品数量
	private double goodsAmount;//退货商品金额
	private int goodsReasonId;//退货理由ID
	private String goodsReason;//退货理由
	private String goodsName;//退货商品名称
	private int productId;
	private int siteskuId;
	
	private List<ReturnOrderGoodsBo> purchaseDetailBo = new ArrayList<ReturnOrderGoodsBo>();

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public int getProType() {
		return proType;
	}

	public void setProType(int proType) {
		this.proType = proType;
	}

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public double getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public int getGoodsReasonId() {
		return goodsReasonId;
	}

	public void setGoodsReasonId(int goodsReasonId) {
		this.goodsReasonId = goodsReasonId;
	}

	public String getGoodsReason() {
		return goodsReason;
	}

	public void setGoodsReason(String goodsReason) {
		this.goodsReason = goodsReason;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public List<ReturnOrderGoodsBo> getPurchaseDetailBo() {
		return purchaseDetailBo;
	}

	public void setPurchaseDetailBo(List<ReturnOrderGoodsBo> purchaseDetailBo) {
		this.purchaseDetailBo = purchaseDetailBo;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getSiteskuId() {
		return siteskuId;
	}

	public void setSiteskuId(int siteskuId) {
		this.siteskuId = siteskuId;
	}
	
}
