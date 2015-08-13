package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_goods")
public class OrderGoods {
	
	@Id
	@GeneratedValue
	@Column(name = "rec_id")
	private int recId;

	/*
	 * 订单号，order表order_id字段
	 */
	@Column(name = "order_id")
	private int orderId = 0;
	
	/*
	 * 产品号，goods表goods_id字段
	 */
	@Column(name = "goods_id")
	private int goodsId = 0;
	
	/*
	 * 产品名，goods表goods_name字段
	 */
	@Column(name = "goods_name")
	private String goodsName = "";
	
	/*
	 * 商品号，goods_spec表spec_id字段
	 */
	@Column(name = "spec_id")
	private int specId = 0;
	
	/*
	 * 商品所属商家id
	 * 
	 */
	@Column(name = "store_id")
	private int storeId = 0;
	
	/*
	 * goods_spec表spec_value_1+spec_value_2
	 */
	@Column(name = "specification")
	private String specification = "";
	
	/*
	 * 商家结算价，即原价
	 */
	@Column(name = "price")
	private double price = 0.0;
	
	/*
	 * 用户应付价格
	 */
	@Column(name = "price_purchase")
	private double pricePurchase = 0.0;
	
	/*
	 * 分摊优惠券后价格
	 */
	@Column(name = "price_discount")
	private double priceDiscount = 0.0;
	
	/*
	 * 购买数量
	 */
	@Column(name = "quantity")
	private int quantity = 0;
	
	/*
	 * 0初态1提货2已对账
	 * 对cvs=0？
	 */
	@Column(name = "goods_status")
	private int goodsStatus = 0;

	/*
	 * 赠送积分
	 * 对cvs=0
	 */
	@Column(name = "points")
	private int points = 0;

	/*
	 * 1正常商品2特价商品3积分换购4组合购买 5快餐 6批发 7赠品 8超值选购 9满减 11量贩团 12:12580会员价商品
	 * 对cvs暂时关注1+2
	 */
	@Column(name = "pro_type")
	private int proType = 0;
	
	/*
	 * 收取佣金
	 * 对cvs=0
	 */
	@Column(name = "commission")
	private double commission = 0.0;
	
	/*
	 * 对cvs来说固定1
	 */
	@Column(name = "is_valid")
	private int isValid = 0;

	/*
	 * 对cvs=0
	 */
	@Column(name = "evaluation")
	private int evaluation = 0;

	/*
	 * 对cvs来说留空字符串，not null
	 */
	@Column(name = "comment")
	private String comment = "";

	/*
	 * 对cvs=0
	 */
	@Column(name = "credit_value")
	private int creditValue = 0;

	/*
	 * 商品图片
	 * 来自goods_image表
	 */
	@Column(name = "goods_image")
	private String goodsImage = "";

	/*
	 * 组合购买的组合ID 或套餐ID
	 * 对应pro_type
	 * 对cvs=0
	 */
	@Column(name = "package_id")
	private Integer packageId = 0;

	/*
	 * 来自goods_spec
	 */
	@Column(name = "taosku")
	private String taosku = "";
	
	/*
	 * 条形码
	 * 来自goods_spec
	 */
	@Column(name = "bn")
	private String bn = "";

	/*
	 * 核验出库，网站上由出库人录入
	 * 对cvs=quantity
	 */
	@Column(name = "outtaostock")
	private int outtaostock = 0;

	/*
	 * 自动生成时间，传null
	 */
	@Column(name = "timestamp")
	private Date timestamp = new Date();

	/*
	 * 对cvs=0
	 */
	@Column(name = "del_flag")
	private int delFlag = 0;

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPricePurchase() {
		return pricePurchase;
	}

	public void setPricePurchase(double pricePurchase) {
		this.pricePurchase = pricePurchase;
	}

	public double getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(double priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(int goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getProType() {
		return proType;
	}

	public void setProType(int proType) {
		this.proType = proType;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(int creditValue) {
		this.creditValue = creditValue;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getTaosku() {
		return taosku;
	}

	public void setTaosku(String taosku) {
		this.taosku = taosku;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public int getOuttaostock() {
		return outtaostock;
	}

	public void setOuttaostock(int outtaostock) {
		this.outtaostock = outtaostock;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
}
