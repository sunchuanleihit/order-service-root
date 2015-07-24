package com.loukou.order.service.enums;


public enum OrderPayTypeEnum {

	TYPE_UNKNOWN(-1),
	
	TYPE_ARRIVAL(1), //货到付款
	
	TYPE_ONLINE(2); //在线支付
	
//	TYPE_JIANHANG(3); //建行员工支付
	
	private int id;
	
	private OrderPayTypeEnum(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
