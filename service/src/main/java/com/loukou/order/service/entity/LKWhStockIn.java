package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name=" lk_wh_stock_in")
public class LKWhStockIn {
	@Id
	@GeneratedValue
	@Column(name="in_id")
	private int inId;
	
	@Column(name="store_id")
	private int storeId;
	
	//采购订单号
	@Column(name="order_no")
	private String orderNo;
	
	//退货订单ID
	@Column(name="order_id_r")
	private int orderIdR;
	
	//1采购入库 2退货入库
	@Column(name="type")
	private int type;
	
	//操作人
	@Column(name="actor")
	private String actor;
	
	@Column(name="create_time")
	private Date createTime;
	
	//备注
	@Column(name="remark")
	private String remark;
	
	//1普票 2增票
	@Column(name="invoice_type")
	private int invoiceType;

	public int getInId() {
		return inId;
	}

	public void setInId(int inId) {
		this.inId = inId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getOrderIdR() {
		return orderIdR;
	}

	public void setOrderIdR(int orderIdR) {
		this.orderIdR = orderIdR;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	
}
