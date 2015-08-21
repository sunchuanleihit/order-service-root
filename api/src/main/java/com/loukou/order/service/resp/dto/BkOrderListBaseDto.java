package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkOrderListBaseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3001614432249750568L;
	private Integer orderId;
	private String orderSn;
	private String orderSnMain;
	private String taoOrderSn;
	private String type;
	private String extension;
	private Integer sellerId;
	private String sellerName;
	private Integer buyerId;
	private String buyerName;
	private Integer status;
	private String statusName;
	private Integer addTime;
	private String addTimeStr;
	private Integer payType;
	private String payTypeToString;
	private Integer payId;
	private String payName;
	private Integer payStatus;
	private Integer payTime;
	private String payTimeToString;
	private String payMessage;
	private Integer shipTime;
	private String shipTimeToString;
	private String invoiceNo;
	private Integer invoiceStatus;
	private Integer invoiceTime;
	private Integer finishedTime;
	private String finishedTimeStr;
	private Double goodsAmount;
	private Double discount;
	private Double orderAmount;
	private Double orderPaid;
	private Double orderNotPaid;
	private Integer needInvoice;
	private Integer invoiceType;
	private String invoiceHeader;
	private String needShipTime;
	private String needShipTimeSlot;
	private Integer evaluationStatus;
	private Integer evaluationTime;
	private Integer anonymous;
	private String postscript;
	private Integer payAlter;
	private Integer mtFlg;
	private Integer statementStatus;
	private Integer source;
	private String sourceName;
	private Integer shippingId;
	private String shipping;
	private Double shippingFee;
	private String useCouponNo;
	private Double useCouponValue;
	private String shippingCompany;
	private String shippingNo;
	private Integer sendReceiptMsg;
	private Integer SendDeliverMsg;
	private String imei;
	private String sellerMemo;
	private String apiOrderSn;
	private Integer printed;
	private Integer isDel;
	private Integer remindTime;
	private String sellSite;
	private String receiveNo;
	
	public String getAddTimeStr() {
		return addTimeStr;
	}
	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}
	public String getFinishedTimeStr() {
		return finishedTimeStr;
	}
	public void setFinishedTimeStr(String finishedTimeStr) {
		this.finishedTimeStr = finishedTimeStr;
	}
	public Double getOrderNotPaid() {
		return orderNotPaid;
	}
	public void setOrderNotPaid(Double orderNotPaid) {
		this.orderNotPaid = orderNotPaid;
	}

	private double totalPrice = 0;
	private double needPayPrice = 0;
	private String shippingType;

	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getOrderSnMain() {
		return orderSnMain;
	}
	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}
	public String getTaoOrderSn() {
		return taoOrderSn;
	}
	public void setTaoOrderSn(String taoOrderSn) {
		this.taoOrderSn = taoOrderSn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getAddTime() {
		return addTime;
	}
	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Integer getPayId() {
		return payId;
	}
	public void setPayId(Integer payId) {
		this.payId = payId;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Integer getPayTime() {
		return payTime;
	}
	public void setPayTime(Integer payTime) {
		this.payTime = payTime;
	}
	public String getPayMessage() {
		return payMessage;
	}
	public void setPayMessage(String payMessage) {
		this.payMessage = payMessage;
	}
	public Integer getShipTime() {
		return shipTime;
	}
	public void setShipTime(Integer shipTime) {
		this.shipTime = shipTime;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public Integer getInvoiceTime() {
		return invoiceTime;
	}
	public void setInvoiceTime(Integer invoiceTime) {
		this.invoiceTime = invoiceTime;
	}
	public Integer getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Integer finishedTime) {
		this.finishedTime = finishedTime;
	}
	public Double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public Double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Double getOrderPaid() {
		return orderPaid;
	}
	public void setOrderPaid(Double orderPaid) {
		this.orderPaid = orderPaid;
	}
	public Integer getNeedInvoice() {
		return needInvoice;
	}
	public void setNeedInvoice(Integer needInvoice) {
		this.needInvoice = needInvoice;
	}
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoiceHeader() {
		return invoiceHeader;
	}
	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}
	public String getNeedShipTime() {
		return needShipTime;
	}
	public void setNeedShipTime(String needShipTime) {
		this.needShipTime = needShipTime;
	}
	public String getNeedShipTimeSlot() {
		return needShipTimeSlot;
	}
	public void setNeedShipTimeSlot(String needShipTimeSlot) {
		this.needShipTimeSlot = needShipTimeSlot;
	}
	public Integer getEvaluationStatus() {
		return evaluationStatus;
	}
	public void setEvaluationStatus(Integer evaluationStatus) {
		this.evaluationStatus = evaluationStatus;
	}
	public Integer getEvaluationTime() {
		return evaluationTime;
	}
	public void setEvaluationTime(Integer evaluationTime) {
		this.evaluationTime = evaluationTime;
	}
	public Integer getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(Integer anonymous) {
		this.anonymous = anonymous;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public Integer getPayAlter() {
		return payAlter;
	}
	public void setPayAlter(Integer payAlter) {
		this.payAlter = payAlter;
	}
	public Integer getMtFlg() {
		return mtFlg;
	}
	public void setMtFlg(Integer mtFlg) {
		this.mtFlg = mtFlg;
	}
	public Integer getStatementStatus() {
		return statementStatus;
	}
	public void setStatementStatus(Integer statementStatus) {
		this.statementStatus = statementStatus;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public Integer getShippingId() {
		return shippingId;
	}
	public void setShippingId(Integer shippingId) {
		this.shippingId = shippingId;
	}
	public Double getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(Double shippingFee) {
		this.shippingFee = shippingFee;
	}
	public String getUseCouponNo() {
		return useCouponNo;
	}
	public void setUseCouponNo(String useCouponNo) {
		this.useCouponNo = useCouponNo;
	}
	public Double getUseCouponValue() {
		return useCouponValue;
	}
	public void setUseCouponValue(Double useCouponValue) {
		this.useCouponValue = useCouponValue;
	}
	public String getShippingCompany() {
		return shippingCompany;
	}
	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}
	public String getShippingNo() {
		return shippingNo;
	}
	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}
	public Integer getSendReceiptMsg() {
		return sendReceiptMsg;
	}
	public void setSendReceiptMsg(Integer sendReceiptMsg) {
		this.sendReceiptMsg = sendReceiptMsg;
	}
	public Integer getSendDeliverMsg() {
		return SendDeliverMsg;
	}
	public void setSendDeliverMsg(Integer sendDeliverMsg) {
		SendDeliverMsg = sendDeliverMsg;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getSellerMemo() {
		return sellerMemo;
	}
	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}
	public String getApiOrderSn() {
		return apiOrderSn;
	}
	public void setApiOrderSn(String apiOrderSn) {
		this.apiOrderSn = apiOrderSn;
	}
	public Integer getPrinted() {
		return printed;
	}
	public void setPrinted(Integer printed) {
		this.printed = printed;
	}
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	public Integer getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(Integer remindTime) {
		this.remindTime = remindTime;
	}
	public String getSellSite() {
		return sellSite;
	}
	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}
	public String getReceiveNo() {
		return receiveNo;
	}
	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}
	public String getPayTimeToString() {
		return payTimeToString;
	}
	public void setPayTimeToString(String payTimeToString) {
		this.payTimeToString = payTimeToString;
	}
	public String getShipTimeToString() {
		return shipTimeToString;
	}
	public void setShipTimeToString(String shipTimeToString) {
		this.shipTimeToString = shipTimeToString;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getNeedPayPrice() {
		return needPayPrice;
	}
	public void setNeedPayPrice(double needPayPrice) {
		this.needPayPrice = needPayPrice;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getShipping() {
		return shipping;
	}
	public void setShipping(String shipping) {
		this.shipping = shipping;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getShippingType() {
		return shippingType;
	}
	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}
	public String getPayTypeToString() {
		return payTypeToString;
	}
	public void setPayTypeToString(String payTypeToString) {
		this.payTypeToString = payTypeToString;
	}
	

}
