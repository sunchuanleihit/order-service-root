package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.serverstarted.cart.service.constants.PackageType;

@Entity
@Table(name = "tcz_goods")
public class Goods {

	@Id
	@GeneratedValue
	@Column(name = "goods_id")
	private int goodsId;
	@Column(name = "goods_name")
	private String goodsName;
	@Column(name = "store_id")
	private int storeId;
	@Column(name = "type")
	private String type = PackageType.MATERIAL;
	@Column(name = "showtype")
	private int showType = 0;	// 展示类型，0=销售商家，1=展示商家，2=代客下单 3=丽华快餐辅产品	
	@Column(name = "goodstype")
	private int goodsType = 0;	// 商品类型：0=单一商品、1=打包商品
	@Column(name = "new_cate_id")
	private int newCateId;
	@Column(name = "new_cate_id_1")
	private int newCateIdOne;
	@Column(name = "new_cate_id_2")
	private int newCateIdTwo;
	@Column(name = "new_cate_id_3")
	private int newCateIdThree;
	@Column(name = "if_show")
	private int ifShow = 1;	// 是否显示（默认为1上架、0为下架）
	@Column(name = "closed")
	private int closed = 1;	// 审核商品，0为审核通过，1为待审核，2为审核未通过，3为逻辑删除
	@Column(name = "default_image")
	private String defaultImage = "";	// 默认图片地址
	@Column(name = "sell_site")
	private String sellSite = "";	// 站点
	@Column(name = "is_del")
	private int isDel = 0;
	@Column(name = "new_cate_name")
	private String newCateName = "";
	@Column(name = "new_brand_id")
	private String newBrandId = "0";
	@Column(name = "new_brand")
	private String newBrand = "";
	@Column(name = "commission")
	private double commission;
	@Column(name = "spec_name_1")
	private String specName1= "";
	@Column(name = "spec_name_2")
	private String specName2= "";
	@Column(name = "readytime")
	private int readyTime = 0;
	
	public String getNewBrandId() {
		return newBrandId;
	}

	public void setNewBrandId(String newBrandId) {
		this.newBrandId = newBrandId;
	}
	public int getNewCateIdThree() {
		return newCateIdThree;
	}

	public void setNewCateIdThree(int newCateIdThree) {
		this.newCateIdThree = newCateIdThree;
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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getNewCateId() {
		return newCateId;
	}

	public void setNewCateId(int newCateId) {
		this.newCateId = newCateId;
	}

	public int getNewCateIdOne() {
		return newCateIdOne;
	}

	public void setNewCateIdOne(int newCateIdOne) {
		this.newCateIdOne = newCateIdOne;
	}

	public int getNewCateIdTwo() {
		return newCateIdTwo;
	}

	public void setNewCateIdTwo(int newCateIdTwo) {
		this.newCateIdTwo = newCateIdTwo;
	}

	public int getIfShow() {
		return ifShow;
	}

	public void setIfShow(int ifShow) {
		this.ifShow = ifShow;
	}

	public int getClosed() {
		return closed;
	}

	public void setClosed(int closed) {
		this.closed = closed;
	}

	public String getDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}

	public String getSellSite() {
		return sellSite;
	}

	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public String getNewCateName() {
		return newCateName;
	}

	public void setNewCateName(String newCateName) {
		this.newCateName = newCateName;
	}

	public String getNewBrand() {
		return newBrand;
	}

	public void setNewBrand(String newBrand) {
		this.newBrand = newBrand;
	}

	public String getSpecName1() {
		return specName1;
	}

	public void setSpecName1(String specName1) {
		this.specName1 = specName1;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public int getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime;
	}

	public String getSpecName2() {
		return specName2;
	}

	public void setSpecName2(String specName2) {
		this.specName2 = specName2;
	}
	
}
