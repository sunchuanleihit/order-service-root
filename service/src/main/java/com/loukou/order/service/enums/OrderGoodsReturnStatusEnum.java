package com.loukou.order.service.enums;

public enum OrderGoodsReturnStatusEnum {
	
	STATUS_DEFAULT(0),//未退货
	
	STATUS_RETURNED(1);//已退货
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private OrderGoodsReturnStatusEnum(int id) {
		this.id = id;
	}
}
