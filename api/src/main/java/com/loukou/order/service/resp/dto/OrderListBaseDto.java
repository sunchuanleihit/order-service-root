package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class OrderListBaseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8713392709208727448L;
	private int orderId;
	private String orderSnMain;
	private int sellerId;
	private String source;
	private String state;
	private String addTime;
	private String payTime;
	private String shipTime;
	private int payStatus;
	private int status;
	private String taoOrderSn;
	private int isshouhuo;
	private String totalPrice;
	private String needPayPrice;
	private double shippingFee;
	private String packageStatus;
	private String shipping;
	private String arrivalCode;
	private int commentStatus;
	private String storePhone;
	private String refundStatus;
	private String discount;
	private String shippingtype;
	private String payType;
	private String invoiceHeader;
	private String postscript;
	private String isOrder;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTaoOrderSn() {
		return taoOrderSn;
	}

	public void setTaoOrderSn(String taoOrderSn) {
		this.taoOrderSn = taoOrderSn;
	}

	public int getIsshouhuo() {
		return isshouhuo;
	}

	public void setIsshouhuo(int isshouhuo) {
		this.isshouhuo = isshouhuo;
	}

	public String getNeedPayPrice() {
		return needPayPrice;
	}

	public void setNeedPayPrice(String needPayPrice) {
		this.needPayPrice = needPayPrice;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getPackageStatus() {
		return packageStatus;
	}

	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}

	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getArrivalCode() {
		return arrivalCode;
	}

	public void setArrivalCode(String arrivalCode) {
		this.arrivalCode = arrivalCode;
	}

	public int getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(int commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getStorePhone() {
		return storePhone;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getShippingtype() {
		return shippingtype;
	}

	public void setShippingtype(String shippingtype) {
		this.shippingtype = shippingtype;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public String getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}

    @Override
    public String toString() {
        return "OrderListBaseDto [orderId=" + orderId + ", orderSnMain=" + orderSnMain + ", sellerId=" + sellerId
                + ", source=" + source + ", state=" + state + ", addTime=" + addTime + ", payTime=" + payTime
                + ", shipTime=" + shipTime + ", payStatus=" + payStatus + ", status=" + status + ", taoOrderSn="
                + taoOrderSn + ", isshouhuo=" + isshouhuo + ", totalPrice=" + totalPrice + ", needPayPrice="
                + needPayPrice + ", shippingFee=" + shippingFee + ", packageStatus=" + packageStatus + ", shipping="
                + shipping + ", arrivalCode=" + arrivalCode + ", commentStatus=" + commentStatus + ", storePhone="
                + storePhone + ", refundStatus=" + refundStatus + ", discount=" + discount + ", shippingtype="
                + shippingtype + ", payType=" + payType + ", invoiceHeader=" + invoiceHeader + ", postscript="
                + postscript + ", isOrder=" + isOrder + "]";
    }

}