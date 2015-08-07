package com.loukou.order.service.enums;

public enum CoupListIsCheckedEnum {
	UNCHECKED(0),
	
	CHECKED(1);
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private CoupListIsCheckedEnum(int id) {
		this.id = id;
	}
	
	
}
