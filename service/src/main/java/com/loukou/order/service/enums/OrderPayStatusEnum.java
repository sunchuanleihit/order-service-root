package com.loukou.order.service.enums;


public enum OrderPayStatusEnum {

	STATUS_UNKNOWN(-1, "UNKNOWN"),
	
	STATUS_SUCC(0, "succ"),
	
	STATUS_FAILED(1, "failed"),
	
	STATUS_CANCEL(2, "cancel"),
	
	STATUS_ERROR(3, "error"),
	
	STATUS_INVALID(4, "invalid"),
	
	STATUS_PROGRESS(5, "progress"),
	
	STATUS_TIMEOUT(6, "timeout"),
	
	STATUS_READY(7, "ready");
	
	private int id;
	private String status;
	
	private OrderPayStatusEnum(int id, String status) {
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
