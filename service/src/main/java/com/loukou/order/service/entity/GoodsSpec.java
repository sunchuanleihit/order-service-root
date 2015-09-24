package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tcz_goods_spec")
public class GoodsSpec {

	@Id
	@GeneratedValue
	@Column(name = "spec_id")
	private int specId;
	
	@Column(name = "goods_id")
	private int goodsId;
	
	@Column(name = "taostock")
	private int taostock = 0;
	
	@Column(name = "taosku")
	private String taosku = "";
	
	@Column(name = "freezstock")
	private int freezstock = 0;
	
	@Column(name = "bn")
	private String bn = "";
	
	@Column(name = "price")
	private double price = 0.0;
	
	@Column(name = "spec_1")
	private String specOne = "";
	
	@Column(name = "spec_2")
	private String specTwo = "";
	
	@Column(name = "spec_3")
	private String specThree = "";
	
	@Column(name = "isshow")
	private int isshow = 1;	// 是否显示 默认 1为显示， 0为不显示
	
	@Column(name = "is_del")
	private int isDel = 0;
	
	@Column(name = "is_onespec")
	private int isOnespec = 0;	// 是否是基础规格 0普通规格  1基础规格
	
	@Column(name="product_id")
	private long productId;
	
	@Column(name="sitesku_id")
	private long siteskuId;
	
	
	public String getTaosku() {
		return taosku;
	}

	public void setTaosku(String taosku) {
		this.taosku = taosku;
	}
	@Deprecated
	public int getSpecId() {
		return specId;
	}
	@Deprecated
	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public int getFreezstock() {
		return freezstock;
	}

	public void setFreezstock(int freezstock) {
		this.freezstock = freezstock;
	}

	public int getTaostock() {
		return taostock;
	}

	public void setTaostock(int taostock) {
		this.taostock = taostock;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	@Deprecated
	public int getGoodsId() {
		return goodsId;
	}
	@Deprecated
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getSpecOne() {
		return specOne;
	}

	public void setSpecOne(String specOne) {
		this.specOne = specOne;
	}

	public String getSpecTwo() {
		return specTwo;
	}

	public void setSpecTwo(String specTwo) {
		this.specTwo = specTwo;
	}

	public String getSpecThree() {
		return specThree;
	}

	public void setSpecThree(String specThree) {
		this.specThree = specThree;
	}

	public int getIsshow() {
		return isshow;
	}

	public void setIsshow(int isshow) {
		this.isshow = isshow;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getIsOnespec() {
		return isOnespec;
	}

	public void setIsOnespec(int isOnespec) {
		this.isOnespec = isOnespec;
	}

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getSiteskuId() {
        return siteskuId;
    }

    public void setSiteskuId(long siteskuId) {
        this.siteskuId = siteskuId;
    }
	
}
