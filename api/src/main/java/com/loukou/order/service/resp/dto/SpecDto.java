package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class SpecDto implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -7128000996746443423L;
    private int specId;
    private GoodsInfoDto goodsInfo;
    private String specName;
    private int storeStock; // 微仓库存
    private Double purchasePrice; // 微仓采购价
    private Double sellPrice; // 销售价格
    private int maxPurchaseNum; // 可采量
    private int deliveryNum; // 采购中数量
    private int buyNum; // 购买或采购的数量

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

   

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getStoreStock() {
        return storeStock;
    }

    public void setStoreStock(int storeStock) {
        this.storeStock = storeStock;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getMaxPurchaseNum() {
        return maxPurchaseNum;
    }

    public void setMaxPurchaseNum(int maxPurchaseNum) {
        this.maxPurchaseNum = maxPurchaseNum;
    }

    public int getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(int deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    @Override
    public String toString() {
        return "SpecDto [specId=" + specId + ", goodInfo=" + goodsInfo + ", specName=" + specName + ", storeStock="
                + storeStock + ", purchasePrice=" + purchasePrice + ", sellPrice=" + sellPrice + ", maxPurchaseNum="
                + maxPurchaseNum + ", deliveryNum=" + deliveryNum + ", buyNum=" + buyNum + "]";
    }

    public GoodsInfoDto getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfoDto goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

}
