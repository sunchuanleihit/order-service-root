package com.loukou.order.service.enums;

public enum AsyncTaskStatusEnum {
	STATUS_NEW(0),
	
	STATUS_SUCCESS(1),
	
	STATUS_FAILED(2);
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private AsyncTaskStatusEnum(int id) {
		this.id = id;
	}
}
