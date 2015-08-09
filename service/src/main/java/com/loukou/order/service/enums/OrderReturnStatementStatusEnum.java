package com.loukou.order.service.enums;

public enum OrderReturnStatementStatusEnum {
	//对账状态0未对账1已对账
	STATUS_NO(0),
	
	STATUS_YES(1);
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private OrderReturnStatementStatusEnum(int id) {
		this.id = id;
	}
	
	
}
