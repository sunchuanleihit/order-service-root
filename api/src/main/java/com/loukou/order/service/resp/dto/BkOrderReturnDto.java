package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkOrderReturnDto implements Serializable {
	private static final long serialVersionUID = 3949979643768221237L;
	private Integer orderIdR;
	private Integer orderId;
	private String orderSnMain;
	private Integer buyerId;
	private String buyerName;
	private Integer sellerId;
	private String sellerName;
	private Double returnAmount;
	private Double shippingFee;
	private String actor;
	private String addTime;
	private Integer goodsType;
	private Integer orderType;
	private String orderTypeStr;
	private Integer orderStatus;
	private String orderStatusStr;
	private Integer goodsStatus;
	private String goodsStatusStr;
	private Integer refundStatus;
	private Integer statementStatus;
	private String postscript;
	private String repayTime;
	private Integer printed;
	private Integer conId;
	private Integer reason;
	private Integer repayWay;
	
	public String getOrderTypeStr() {
		return orderTypeStr;
	}
	public void setOrderTypeStr(String orderTypeStr) {
		this.orderTypeStr = orderTypeStr;
	}
	public String getGoodsStatusStr() {
		return goodsStatusStr;
	}
	public void setGoodsStatusStr(String goodsStatusStr) {
		this.goodsStatusStr = goodsStatusStr;
	}
	public String getOrderStatusStr() {
		return orderStatusStr;
	}
	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}
	public Integer getOrderIdR() {
		return orderIdR;
	}
	public void setOrderIdR(Integer orderIdR) {
		this.orderIdR = orderIdR;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public Integer getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public Double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public Double getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(Double shippingFee) {
		this.shippingFee = shippingFee;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public Integer getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	public Integer getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}
	public Integer getStatementStatus() {
		return statementStatus;
	}
	public void setStatementStatus(Integer statementStatus) {
		this.statementStatus = statementStatus;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public Integer getPrinted() {
		return printed;
	}
	public void setPrinted(Integer printed) {
		this.printed = printed;
	}
	public Integer getConId() {
		return conId;
	}
	public void setConId(Integer conId) {
		this.conId = conId;
	}
	public Integer getReason() {
		return reason;
	}
	public void setReason(Integer reason) {
		this.reason = reason;
	}
	public Integer getRepayWay() {
		return repayWay;
	}
	public void setRepayWay(Integer repayWay) {
		this.repayWay = repayWay;
	}
}
