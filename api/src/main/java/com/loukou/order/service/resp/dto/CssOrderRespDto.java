package com.loukou.order.service.resp.dto;

import java.io.Serializable;

/**
 * 
 * @author sunchuanlei
 *
 */
public class CssOrderRespDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6165081602626630342L;
	private Integer orderId;//订单号
	private String orderSnMain;//订单编号
	private String cityName;//城市
	private Integer source;//订单来源
	private String needShiptime;//期望送达时间
	private Integer status;//订单状态
	private Integer needInvoice;//是否开票
	private String invoiceNo;//开票号码
	private String buyerName;//注册用户名
	private String payNames;//付款方式
	private String orderAmount;//订单总额
	private String goodsAmount;//商品总额
	private String hasPaid;//已付金额
	private String balance;//未付金额
	private String consignee;//收件人姓名
	private String regionName;//地区
	private String address;//详细地址
	private String phoneMob;//电话
	private String isPrint;//是否打单
	private String payStatus;//是否付款
	private String finishedTime;//完成时间
	private String isShipped;//是否发货
	private String shipper;//发货人
	private String shipTime;//发货时间
	private String addTime;//订单时间
	private String postScript;//物流提示
	private String payMessage;//订单备注 
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
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getNeedShiptime() {
		return needShiptime;
	}
	public void setNeedShiptime(String needShiptime) {
		this.needShiptime = needShiptime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getNeedInvoice() {
		return needInvoice;
	}
	public void setNeedInvoice(Integer needInvoice) {
		this.needInvoice = needInvoice;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getPayNames() {
		return payNames;
	}
	public void setPayNames(String payNames) {
		this.payNames = payNames;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(String goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public String getHasPaid() {
		return hasPaid;
	}
	public void setHasPaid(String hasPaid) {
		this.hasPaid = hasPaid;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneMob() {
		return phoneMob;
	}
	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}
	public String getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(String finishedTime) {
		this.finishedTime = finishedTime;
	}
	public String getIsShipped() {
		return isShipped;
	}
	public void setIsShipped(String isShipped) {
		this.isShipped = isShipped;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getShipTime() {
		return shipTime;
	}
	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getPostScript() {
		return postScript;
	}
	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}
	public String getPayMessage() {
		return payMessage;
	}
	public void setPayMessage(String payMessage) {
		this.payMessage = payMessage;
	}
	
}
