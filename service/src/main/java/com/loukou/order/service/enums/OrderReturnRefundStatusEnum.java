package com.loukou.order.service.enums;

public enum OrderReturnRefundStatusEnum {
	//是否退款0未退款1已退款
	STATUS_NO(0),
	
	STATUS_YES(1);

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private OrderReturnRefundStatusEnum(int id) {
		this.id = id;
	}
}
