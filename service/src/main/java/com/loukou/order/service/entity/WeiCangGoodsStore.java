package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 每个微仓销售的商品及库存
 * @author laphi
 *
 */
@Entity
@Table(name = "lk_wh_goods_store")
public class WeiCangGoodsStore {

	@Id
	@GeneratedValue
	@Column(name = "gs_id")
	private int gsId;

	@Column(name = "spec_id")
	private int specId = 0;

	@Column(name = "goods_id")
	private int goodsId = 0;

	@Column(name = "store_id")
	private int storeId = 0;

	@Column(name = "stock_w")
	private int storeW = 0;// 警告库存

	@Column(name = "stock_s")
	private int stockS = 0;// 库存

	@Column(name = "freezstock")
	private int freezstock = 0;

	@Column(name = "status")
	private int status = 1;// '1上架 0下架',

	@Column(name = "update_time")
	private Date updateTime = new Date();

	public int getGsId() {
		return gsId;
	}

	public void setGsId(int gsId) {
		this.gsId = gsId;
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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getStoreW() {
		return storeW;
	}

	public void setStoreW(int storeW) {
		this.storeW = storeW;
	}

	public int getStockS() {
		return stockS;
	}

	public void setStockS(int stockS) {
		this.stockS = stockS;
	}

	public int getFreezstock() {
		return freezstock;
	}

	public void setFreezstock(int freezstock) {
		this.freezstock = freezstock;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
