package com.loukou.order.service.enums;

public enum OrderReturnGoodsType {

	//'0退货订单1拒收订单2多付款退款 3退运费 4.客户赔偿 5其他退款,6客户自己取消订单退款  7:特殊退款'
	
	TYPE_BACK(0, "退货订单"),
	TYPE_REFUSE(1, "拒收订单"),
	TYPE_MUTI_REFUND(2, "多付款退款"),
	TYPE_BACK_FEE(3, "退运费"),
	TYPE_REPAY(4, "客户赔偿"),
	TYPE_OTHER(5, "其他退款"),
	TYPE_SELF_CANCEL(6, "客户自己取消订单退款"),
	TYPE_SPECEL(7, "特殊退款"),
	TYPE_UNKNOWN(-1, "");
	
	OrderReturnGoodsType(int id, String type) {
		this.id = id;
		this.type = type;
	}
	
	private int id;
	private String type;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public static OrderReturnGoodsType parseType(int id){
		for (OrderReturnGoodsType e : OrderReturnGoodsType.values()) {
			if (e.id == id) {
				return e;
			}
		}
		return TYPE_UNKNOWN;
	}
	
}

	
