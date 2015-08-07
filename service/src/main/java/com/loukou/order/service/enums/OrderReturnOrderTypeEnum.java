package com.loukou.order.service.enums;

public enum OrderReturnOrderTypeEnum {
	//0退货订单1拒收订单2多付款退款 3退运费 4.客户赔偿 5其他退款,6客户自己取消订单退款 7:特殊退款
	TYPE_RETURN(0);

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private OrderReturnOrderTypeEnum(int id) {
		this.id = id;
	}
}
