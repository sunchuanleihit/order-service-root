package com.loukou.order.service.enums;

public enum OrderReturnOrderStatusEnum {
	//0正常1取消
	STATUS_NOMAL(0),
	
	STATUS_CANCEL(1);

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private OrderReturnOrderStatusEnum(int id) {
		this.id = id;
	}
}
