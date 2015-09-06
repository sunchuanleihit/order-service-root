package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class GoodsListDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6150083226198763019L;
	private String goodsImage = "";
	private String goodsName = "";
	private String specification = "";
	private double pricePurchase = 0;
	private double priceDiscount = 0;
	private int quantity = 0;
	private Integer returnQuantity;
	private int specId = 0;
	private int proType = 0;
	private int goodsId = 0;
	private String bn = "";
	private String taosku = "";
	private double returnMoney = 0;
	private int recId;

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public double getPricePurchase() {
		return pricePurchase;
	}

	public void setPricePurchase(double pricePurchase) {
		this.pricePurchase = pricePurchase;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

    @Override
    public String toString() {
        return "GoodsListDto [goodsImage=" + goodsImage + ", goodsName=" + goodsName + ", specification="
                + specification + ", pricePurchase=" + pricePurchase + ", quantity=" + quantity + ", specId=" + specId
                + ", goodsId=" + goodsId + "]";
    }

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public String getTaosku() {
		return taosku;
	}

	public void setTaosku(String taosku) {
		this.taosku = taosku;
	}

	public double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(double returnMoney) {
		this.returnMoney = returnMoney;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public int getProType() {
		return proType;
	}

	public void setProType(int proType) {
		this.proType = proType;
	}

	public double getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(double priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}
}
