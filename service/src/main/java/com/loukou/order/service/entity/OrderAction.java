package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_action")
public class OrderAction {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	/*
	 * 0.下单 3.审单 5.取货 14.发货 15.回单 20.选择支付方式
	 */
	@Column(name = "action")
	private int action = 0;
	
	/*
	 * 主单号
	 * 如141107091685349
	 */
	@Column(name = "order_sn_main")
	private String orderSnMain = "";

	/*
	 * 包裹单号
	 */
	@Column(name = "tao_order_sn")
	private String taoOrderSn = "";

	/*
	 * 子订单号
	 */
	@Column(name = "order_id")
	private Integer orderId = 0;
	
	/*
	 * 操作人
	 */
	@Column(name = "actor")
	private String actor = "";

	/*
	 * 操作时间
	 */
	@Column(name = "action_time")
	private Date actionTime = new Date();

	/*
	 * 操作说明
	 */
	@Column(name = "notes")
	private String notes = "";

	/*
	 * 记录插入时间
	 */
	@Column(name = "timestamp")
	private Date timestamp = new Date();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
