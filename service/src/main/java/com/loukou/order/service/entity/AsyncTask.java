package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tcz_async_task")
public class AsyncTask {
	
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name = "order_id")
	private int orderId;
	
	/*
	 * 包裹单号
	 */
	@Column(name = "tao_order_sn")
	private String taoOrderSn;
	
	/*
	 * 主单号
	 * 如141107091685349
	 */
	@Column(name = "order_sn_main")
	private String orderSnMain;
	
	/**
	 * 操作，对应AsyncTaskActionEnum
	 */
	@Column(name="action")
	private int action;
	
	/**
	 * 状态，对应AsyncTaskStatusEnum
	 */
	@Column(name="status")
	private int status;
	
	@Column(name="execute_message")
	private String executeMessage;
	
	@Column(name="action_key")
	private int actionKey;
	
	@Column(name="create_time")
	private Date createTime;
	
	/**
	 * 完成时间
	 */
	@Column(name="complate_time")
	private Date complateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getTaoOrderSn() {
		return taoOrderSn;
	}

	public void setTaoOrderSn(String taoOrderSn) {
		this.taoOrderSn = taoOrderSn;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getActionKey() {
		return actionKey;
	}

	public void setActionKey(int actionKey) {
		this.actionKey = actionKey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getComplateTime() {
		return complateTime;
	}

	public void setComplateTime(Date complateTime) {
		this.complateTime = complateTime;
	}

	public String getExecuteMessage() {
		return executeMessage;
	}

	public void setExecuteMessage(String executeMessage) {
		this.executeMessage = executeMessage;
	}
	
	
}

