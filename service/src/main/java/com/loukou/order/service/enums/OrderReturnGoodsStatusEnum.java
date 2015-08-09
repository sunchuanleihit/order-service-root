package com.loukou.order.service.enums;

public enum OrderReturnGoodsStatusEnum {
	STATUS_DEFAULT(0),
	
	STATUS_RETURNED(4);
	//0未取货1已取货2损耗3待退商家4已退商家
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private OrderReturnGoodsStatusEnum(int id) {
		this.id = id;
	}
}
