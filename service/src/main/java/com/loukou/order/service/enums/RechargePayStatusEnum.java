package com.loukou.order.service.enums;


public enum RechargePayStatusEnum {

	STATUS_UNKNOWN(-1, "UNKNOWN"),
	
	STATUS_INIT(0, "init"),
	
	STATUS_SUCCESS(1, "success"),
	
	STATUS_FAILED(2, "failed");
	
	private int id;
	private String status;
	
	private RechargePayStatusEnum(int id, String status) {
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
