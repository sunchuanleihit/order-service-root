package com.loukou.order.service.enums;


public enum PayStatusEnum {
	//0未付款1已付款2部分付款
	STATUS_UNPAY(0, "未付款"),
	
	STATUS_PAYED(1, "已付款"),
	
	STATUS_PART_PAYED(2, "部分付款");
	
	private int id;
	private String status;
	
	private PayStatusEnum(int id, String status) {
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
