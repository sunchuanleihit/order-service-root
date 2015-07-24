package com.loukou.order.service.enums;


public enum PaySignStatusEnum {
	//'fail','succ','ready
	STATUS_FAIL("fail"),
	
	STATUS_SUCC("succ"),
	
	STATUS_READY("ready");
	
	private String status;
	
	private PaySignStatusEnum( String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
