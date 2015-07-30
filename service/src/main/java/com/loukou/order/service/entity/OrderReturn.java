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
	private Integer buyerId;

	/*
	 * 商家id，淘常州自营会细分 通过goods_spec关联到good_id 通过goods表关联到store_id
	 */
	@Column(name = "seller_id")
	private Integer sellerId;

	/**
	 * 退款金额(包含运费)
	 */
	@Column(name = "return_amount")
	private Double returnAmount;

	/*
	 * 物流费 对cvs=0
	 */
	@Column(name = "shipping_fee")
	private Double shippingFee;

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

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
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
