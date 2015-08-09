package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tcz_order")
public class Order {
	
	@Id
	@GeneratedValue
	@Column(name = "order_id")
	private int orderId;

	/*
	 * 子单号
	 * 如1431056886
	 */
	@Column(name = "order_sn")
	private String orderSn = "";
	
	/*
	 * 主单号
	 * 如141107091685349
	 */
	@Column(name = "order_sn_main")
	private String orderSnMain = "";
	
	/*
	 * 包裹单号
	 * 如141107091685349-2-1 
	 */
	@Column(name = "tao_order_sn")
	private String taoOrderSn = "";
	
	/*
	 * material=普通商品 service=服务 3-话费充值
	 * 对cvs都是普通商品
	 */
	@Column(name = "type")
	private String type = "";
	
	/*
	 * 没用，留空字符串，not null
	 */
	@Column(name = "extension")
	private String extension = "";
	
	/*
	 * 商家id，淘常州自营会细分
	 * 通过goods_spec关联到good_id
	 * 通过goods表关联到store_id
	 */
	@Column(name = "seller_id")
	private int sellerId = 0;
	
	/*
	 * 商家名
	 */
	@Column(name = "seller_name")
	private String sellerName = "";
	
	/*
	 * 对应member表user_id
	 */
	@Column(name = "buyer_id")
	private int buyerId = 0;
	
	/*
	 * 对应member表real_name
	 */
	@Column(name = "buyer_name")
	private String buyerName = "";
	
	/*
	 * 订单状态
	 * 对cvs线下订单直接回单=15
	 */
	@Column(name = "status")
	private int status = 0;
	
	/*
	 * 下单时间
	 */
	@Column(name = "add_time")
	private Integer addTime = 0;

	/*
	 * 1货到付款2在线支付3建行员工支付
	 * 对cvs来说固定1
	 */
	@Column(name = "pay_type")
	private int payType = 0;

	/*
	 * 支付方式，payment表中payment_id
	 * 多支付方式订单只记录最后选的一种
	 * 对cvs来说固定现金支付=1
	 */
	@Column(name = "pay_id")
	private int payId = 0;
	
	/*
	 * 支付方式名称，对应pay_id
	 * payment表中payment_name
	 */
	@Column(name = "pay_name")
	private String payName = "";
	
	/*
	 * 支付状态，0未付款1已付款2部分付款
	 * 对cvs来说固定1
	 */
	@Column(name = "pay_status")
	private int payStatus = 0;

	/*
	 * 付款时间
	 * 对cvs来说是下单时间
	 */
	@Column(name = "pay_time")
	private Integer payTime = 0;

	/*
	 * 对cvs来说留空字符串，not null
	 */
	@Column(name = "pay_message")
	private String payMessage = "";

	/*
	 * 发货时间
	 * 对cvs来说null
	 */
	@Column(name = "ship_time")
	private Integer shipTime = 0;

	/*
	 * 发票号，发货前录入
	 * 对cvs来说留空，null
	 */
	@Column(name = "invoice_no")
	private String invoiceNo = "";

	/*
	 * 0未开票1已开票2已取票
	 * 对cvs来说固定0
	 */
	@Column(name = "invoice_status")
	private int invoiceStatus = 0;

	/*
	 * 开票时间
	 * 对cvs来说留空null
	 */
	@Column(name = "invoice_time")
	private Integer invoiceTime = 0;
	
	/*
	 * 收货时间 not null
	 * 对cvs来说0
	 */
	@Column(name = "finished_time")
	private Integer finishedTime = 0;
	
	/*
	 * 商品总价（不含运费）
	 * 对特价商品是特价后价格
	 */
	@Column(name = "goods_amount")
	private double goodsAmount = 0.0;

	/*
	 * 折扣价格
	 * 当前包括优惠券+满减
	 */
	@Column(name = "discount")
	private double discount = 0.0;

	/*
	 * 订单金额（商品金额-折扣金额），即应付金额
	 */
	@Column(name = "order_amount")
	private double orderAmount = 0.0;

	/*
	 * 已经支付的金额
	 * 对cvs来说等于orderAmount
	 */
	@Column(name = "order_payed")
	private double orderPayed = 0.0;

	/*
	 * 0不要开票，1需要开票，2发送开票提醒 3.需要开增票
	 * 对cvs来说固定=0
	 */
	@Column(name = "need_invoice")
	private int needInvoice = 0;
	
	/*
	 * 发票类型，'1普票 2增票'
	 * 
	 */
	@Column(name = "invoice_type")
	private int invoiceType = 0;

	/*
	 * 发票抬头
	 * 对cvs来说留空null
	 */
	@Column(name = "invoice_header")
	private String invoiceHeader = "";

	/*
	 * 期望配送时间
	 * 对cvs留空null
	 */
	@Column(name = "need_shiptime")
	private Date needShiptime = null;

	/*
	 * 期望送货时间段
	 * 对cvs留空null
	 */
	@Column(name = "need_shiptime_slot")
	private String needShiptimeSlot = null;

	/*
	 * 评价状态
	 * 对cvs来说=0
	 */
	@Column(name = "evaluation_status")
	private int evaluationStatus = 0;

	/*
	 * 评价时间
	 * 对cvs留空=0
	 */
	@Column(name = "evaluation_time")
	private int evaluationTime = 0;

	/*
	 * 对cvs=0
	 */
	@Column(name = "anonymous")
	private int anonymous = 0;

	/*
	 * 商家留言
	 * 对cvs留空，not null
	 */
	@Column(name = "postscript")
	private String postscript = "";

	/*
	 * 对cvs=0
	 */
	@Column(name = "pay_alter")
	private int payAlter = 0;

	/*
	 * 订单内容传输：0未传输1已传输
	 * 对cvs插入时=0
	 */
	@Column(name = "mt_flg")
	private int mtFlg = 0;

	/*
	 * 0 未加入对账单 1 已加入对账单
	 * 对cvs=0
	 */
	@Column(name = "statement_status")
	private int statementStatus = 0;

	/*
	 * 订单来源
	 * 对cvs=50
	 */
	@Column(name = "source")
	private int source = 0;
	
	@Column(name = "sell_site")
	private String sellSite = "";

	/*
	 * 配送方式(0黄蜂1商家)
	 * 对cvs=1？
	 */
	@Column(name = "shipping_id")
	private int shippingId = 0;

	/*
	 * 物流费
	 * 对cvs=0
	 */
	@Column(name = "shipping_fee")
	private double shippingFee = 0.0;

	/*
	 * 使用优惠券号
	 * 对cvs留空字符串
	 */
	@Column(name = "use_coupon_no")
	private String useCouponNo = "";

	/*
	 * 优惠券金额
	 * 对cvs暂时为0
	 */
	@Column(name = "use_coupon_value")
	private double useCouponValue = 0.0;

	/*
	 * 自动生成的时间戳
	 * 留空null
	 */
	@Column(name = "timestamp")
	private Date timestamp = new Date();

	/*
	 * 对cvs留空null
	 */
	@Column(name = "shipping_company")
	private String shippingCompany = "";

	/*
	 * 对cvs留空null
	 */
	@Column(name = "shipping_no")
	private String shippingNo = "";

	/*
	 * 是否发送过确认收货短信 0 否 1是
	 * 对cvs=0
	 */
	@Column(name = "send_receipt_msg")
	private int sendReceiptMsg = 0;

	/*
	 * 是否发送过发货提供短信 0-否 1-是
	 * 对cvs=0
	 */
	@Column(name = "send_deliver_msg")
	private int sendDeliverMsg = 0;

	/*
	 * 手机端标识
	 * 对cvs留空null
	 */
	@Column(name = "imei")
	private String imei = "";

	/*
	 * 商家留言备注
	 * 对cvs留空null
	 */
	@Column(name = "seller_memo")
	private String sellerMemo = "";

	/*
	 * 合作网站订单号
	 * 对cvs留空null
	 */
	@Column(name = "api_order_sn")
	private String apiOrderSn = "";

	/*
	 * 0-都未打印,1-面单已打印,2-订单已打印,3-面单订单都已打印
	 * 对cvs=0?
	 */
	@Column(name = "printed")
	private int printed = 0;

	/*
	 * 用户自行删除，0未删除,1已删除
	 * 对cvs=0
	 */
	@Column(name = "is_del")
	private int isDel = 0;

	@Column(name="receive_no")
	private String receiveNo;
	
	

    public String getReceiveNo() {
        return receiveNo;
    }

    public void setReceiveNo(String receiveNo) {
        this.receiveNo = receiveNo;
    }

    public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
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

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public int getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getAddTime() {
		return addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
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

	public int getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(int invoiceStatus) {
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

	public double getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public double getOrderPayed() {
		return orderPayed;
	}

	public void setOrderPayed(double orderPayed) {
		this.orderPayed = orderPayed;
	}

	public int getNeedInvoice() {
		return needInvoice;
	}

	public void setNeedInvoice(int needInvoice) {
		this.needInvoice = needInvoice;
	}

	public String getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public Date getNeedShiptime() {
		return needShiptime;
	}

	public void setNeedShiptime(Date needShiptime) {
		this.needShiptime = needShiptime;
	}

	public String getNeedShiptimeSlot() {
		return needShiptimeSlot;
	}

	public void setNeedShiptimeSlot(String needShiptimeSlot) {
		this.needShiptimeSlot = needShiptimeSlot;
	}

	public int getEvaluationStatus() {
		return evaluationStatus;
	}

	public void setEvaluationStatus(int evaluationStatus) {
		this.evaluationStatus = evaluationStatus;
	}

	public int getEvaluationTime() {
		return evaluationTime;
	}

	public void setEvaluationTime(int evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	public int getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(int anonymous) {
		this.anonymous = anonymous;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public int getPayAlter() {
		return payAlter;
	}

	public void setPayAlter(int payAlter) {
		this.payAlter = payAlter;
	}

	public int getMtFlg() {
		return mtFlg;
	}

	public void setMtFlg(int mtFlg) {
		this.mtFlg = mtFlg;
	}

	public int getStatementStatus() {
		return statementStatus;
	}

	public void setStatementStatus(int statementStatus) {
		this.statementStatus = statementStatus;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getShippingId() {
		return shippingId;
	}

	public void setShippingId(int shippingId) {
		this.shippingId = shippingId;
	}

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getUseCouponNo() {
		return useCouponNo;
	}

	public void setUseCouponNo(String useCouponNo) {
		this.useCouponNo = useCouponNo;
	}

	public double getUseCouponValue() {
		return useCouponValue;
	}

	public void setUseCouponValue(double useCouponValue) {
		this.useCouponValue = useCouponValue;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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

	public int getSendReceiptMsg() {
		return sendReceiptMsg;
	}

	public void setSendReceiptMsg(int sendReceiptMsg) {
		this.sendReceiptMsg = sendReceiptMsg;
	}

	public int getSendDeliverMsg() {
		return sendDeliverMsg;
	}

	public void setSendDeliverMsg(int sendDeliverMsg) {
		this.sendDeliverMsg = sendDeliverMsg;
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

	public int getPrinted() {
		return printed;
	}

	public void setPrinted(int printed) {
		this.printed = printed;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getSellSite() {
		return sellSite;
	}

	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}
}
