package com.loukou.order.service.enums;

public enum ReturnOrderStatus {
	NORMAL(0, "正常"),
	CANCEL(1, "取消");
	
	private int id;
	private String status;
	
	ReturnOrderStatus(int id, String status) {
		this.id = id;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
