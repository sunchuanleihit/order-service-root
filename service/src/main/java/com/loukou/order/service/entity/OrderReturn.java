package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_return")
public class OrderReturn {

	@Id
	@GeneratedValue
	@Column(name = "order_id_r")
	private int orderIdR;

	@Column(name = "order_id")
	private Integer orderId;

	/*
	 * 主单号 如141107091685349
	 */
	@Column(name = "order_sn_main")
	private String orderSnMain;

	/*
	 * 对应member表user_id
	 */
	@Column(name = "buyer_id")
	private int buyerId;

	/*
	 * 商家id，淘常州自营会细分 通过goods_spec关联到good_id 通过goods表关联到store_id
	 */
	@Column(name = "seller_id")
	private int sellerId;

	/**
	 * 退款金额(包含运费)
	 */
	@Column(name = "return_amount")
	private double returnAmount;

	/*
	 * 物流费 对cvs=0
	 */
	@Column(name = "shipping_fee")
	private double shippingFee;

	/*
	 * 操作人
	 */
	@Column(name = "actor")
	private String actor;

	/*
	 * 下单时间
	 */
	@Column(name = "add_time")
	private String addTime;

	/*
	 * 0商品订单1服务订单2jiazheng
	 */
	@Column(name = "goods_type")
	private Integer goodsType;

	/*
	 * 0退货订单1拒收订单2多付款退款 3退运费 4.客户赔偿 5其他退款,6客户自己取消订单退款 7:特殊退款
	 */
	@Column(name = "order_type")
	private Integer orderType;

	/*
	 * 0正常1取消
	 */
	@Column(name = "order_status")
	private Integer orderStatus;

	/*
	 * 0未取货1已指派2损耗3待退商家4已退商
	 */
	@Column(name = "goods_status")
	private Integer goodsStatus;

	/*
	 * 是否退款0未退款1已退款
	 */
	@Column(name = "refund_status")
	private Integer refundStatus;

	/*
	 * 对账状态0未对账1已对账
	 */
	@Column(name = "statement_status")
	private Integer statementStatus;

	/*
	 * 备注
	 */
	@Column(name = "postscript")
	private String postscript;

	/*
	 * 退款时间
	 */
	@Column(name = "repay_time")
	private String repayTime;

	/*
	 * 0未打印 1已打印
	 */
	@Column(name = "printed")
	private Integer printed;

	/*
	 * 消费码主键
	 */
	@Column(name = "con_id")
	private Integer conId;

	/*
	 * 1:超单 2:客户取消订单 3:退运费 4:客户赔偿 5:其它
	 */
	@Column(name = "reason")
	private Integer reason;

	/*
	 * 0虚拟账户1原路返回
	 */
	@Column(name = "repay_way")
	private Integer repayWay;

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

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public int getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
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

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(int goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public int getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
	}

	public int getStatementStatus() {
		return statementStatus;
	}

	public void setStatementStatus(int statementStatus) {
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

	public int getPrinted() {
		return printed;
	}

	public void setPrinted(int printed) {
		this.printed = printed;
	}

	public int getConId() {
		return conId;
	}

	public void setConId(int conId) {
		this.conId = conId;
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public int getRepayWay() {
		return repayWay;
	}

	public void setRepayWay(int repayWay) {
		this.repayWay = repayWay;
	}

}
