package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name=" lk_wh_stock_in_goods")
public class LKWhStockInGoods {
	@Id
	@GeneratedValue
	@Column(name="in_goods_id")
	private int inGoodsId;
	
	@Column(name="in_id")
	private int inId;
	
	@Column(name="spec_id")
	private int specId;
	
	@Column(name="goods_id")
	private int goodsId;
	
	@Column(name="stock")
	private int stock;
	
	@Column(name="product_id")
	private int productId;
	
	@Column(name="sitesku_id")
	private int siteSkuId;

	public int getInGoodsId() {
		return inGoodsId;
	}

	public void setInGoodsId(int inGoodsId) {
		this.inGoodsId = inGoodsId;
	}

	public int getInId() {
		return inId;
	}

	public void setInId(int inId) {
		this.inId = inId;
	}

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getSiteSkuId() {
		return siteSkuId;
	}

	public void setSiteSkuId(int siteSkuId) {
		this.siteSkuId = siteSkuId;
	}
	
	
}
