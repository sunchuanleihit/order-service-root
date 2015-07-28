package com.loukou.order.service.enums;


public enum OrderActionTypeEnum {

	TYPE_UNKNOWN(-1),
	
	TYPE_ORDER(0), //下单
	
	TYPE_INSPECTED(3), //审单
	
	TYPE_CLAIMED(5), //取货
	
	TYPE_DELIVERED(14), //发货
	
	TYPE_SLIP(15), //回单
	
	TYPE_CHOOSE_PAY(20), //选择支付方式
	
	TYPE_33(33),

	TYPE_RETURN_STORAGE(30); //退货入库
	
	private int id;
	
	private OrderActionTypeEnum(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
