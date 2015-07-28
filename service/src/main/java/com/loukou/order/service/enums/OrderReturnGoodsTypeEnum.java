package com.loukou.order.service.enums;

public enum OrderReturnGoodsTypeEnum {
	
	//0商品订单1服务订单2jiazheng
	TYPE_GOODSORDER(0),
	
	TYPE_SERVICEORDER(1),
	
	TYPE_JIAZHENGORDER(1);

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private OrderReturnGoodsTypeEnum(int id) {
		this.id = id;
	}
}
