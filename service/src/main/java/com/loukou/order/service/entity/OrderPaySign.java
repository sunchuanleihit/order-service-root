package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_order_paysign")
public class OrderPaySign {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	/*
	 * 主单号 如141107091685349
	 */
	@Column(name = "order_sn_main")
	private String orderSnMain = "";

	/*
	 * 外部订单号 如141107091685349
	 */
	@Column(name = "out_order_sn")
	private String outOrderSn = "";

	/*
	 * 支付开始时间
	 */
	@Column(name = "pay_id")
	private int payId = 0;

	/*
	 * 支付开始时间
	 */
	@Column(name = "ctime")
	private long cTime = 0;// 支付开始时间

	/*
	 * 支付成功时间
	 */
	@Column(name = "ftime")
	private long fTime = 0;// 支付成功时间

	@Column(name = "money")
	private double money = 0.0;
	
	/*
	 * 支付状态 enum(fail','succ','ready)
	 */
	@Column(name = "status")
	private String status = "";

	/*
	 * 回调数据 serialize 字串
	 */
	@Column(name = "return_data")
	private String returnData = "";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public String getOutOrderSn() {
		return outOrderSn;
	}

	public void setOutOrderSn(String outOrderSn) {
		this.outOrderSn = outOrderSn;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public long getcTime() {
		return cTime;
	}

	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

	public long getfTime() {
		return fTime;
	}

	public void setfTime(long fTime) {
		this.fTime = fTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReturnData() {
		return returnData;
	}

	public void setReturnData(String returnData) {
		this.returnData = returnData;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

}
