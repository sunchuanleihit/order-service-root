package com.loukou.order.service.enums;

public enum ReturnGoodsStatus {
	//0未取货1已指派2损耗3待退商家4已退商家
	UNPICK(0, "未取货"),
	ASSIGN(1, "已指派"),
	WASTAGE(2,"损耗"),
	TO_BACK(3, "待退商家"),
	BACKED(4, "已退商家");
	
	private int id;
	private String status;
	
	ReturnGoodsStatus(int id, String status) {
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
