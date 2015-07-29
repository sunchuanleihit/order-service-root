package com.loukou.order.service.enums;


public enum OrderPayTypeEnum {

	TYPE_UNKNOWN(-1, ""),
	
	TYPE_ARRIVAL(1, "货到付款"), //货到付款
	
	TYPE_ONLINE(2, "在线支付"); //在线支付
	
//	TYPE_JIANHANG(3); //建行员工支付
	
	private int id;
	private String type;
	
	private OrderPayTypeEnum(int id, String type) {
		this.id = id;
		this.type = type;
	}
	
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
	
	
	public static OrderPayTypeEnum parseType(int id) {
		for (OrderPayTypeEnum e : OrderPayTypeEnum.values()) {
			if (e.id == id) {
				return e;
			}
		}
		return TYPE_UNKNOWN;
	}
}
