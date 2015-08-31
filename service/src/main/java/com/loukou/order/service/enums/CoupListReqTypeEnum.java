package com.loukou.order.service.enums;

public enum CoupListReqTypeEnum {
	
	ALL(0, "全部优惠券"),
	
	USABLE(1, "可用优惠券");
	
	private int id;
	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private CoupListReqTypeEnum(int id, String comment) {
		this.id = id;
		this.comment = comment;
	}
	
	
}
