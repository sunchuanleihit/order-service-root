package com.loukou.order.service.enums;

public enum AsyncTaskActionEnum {
	ACTION_REFUND(1);//退款
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private AsyncTaskActionEnum(int id) {
		this.id = id;
	}
	
	
}
