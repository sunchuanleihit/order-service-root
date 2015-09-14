package com.loukou.order.service.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_goods_r")
public class OrderGoodsR {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "order_id_r")
    private int orderIdR;
    
    @Column(name = "order_id")
    private int orderId;
    
    @Column(name = "rec_id")
    private int recId;
    
    @Column(name = "goods_id")
    private int goodsId;
    
    @Column(name = "spec_id")
    private int specId;
    
    @Column(name = "goods_name")
    private String goodsName;
    
    @Column(name = "goods_num")
    private int goodsNum;
    
    @Column(name="price")
    private double price;

    @Column(name="add_time")
    private String addTime;
    
    @Column(name="pro_type")
    private int proType;
    
    @Column(name="goods_amount")
    private double goodsAmount;
    
    @Column(name="timestamp")
    private Timestamp timestamp;
    
    @Column(name = "product_id")
    private int productId = 0;

    @Column(name = "sitesku_id")
    private int siteskuId = 0;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderIdR() {
        return orderIdR;
    }

    public void setOrderIdR(int orderIdR) {
        this.orderIdR = orderIdR;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

    public int getProType() {
        return proType;
    }

    public void setProType(int proType) {
        this.proType = proType;
    }

    public double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    
}
