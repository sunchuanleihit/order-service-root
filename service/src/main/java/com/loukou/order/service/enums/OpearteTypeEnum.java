package com.loukou.order.service.enums;


public enum OpearteTypeEnum {
	//operate_type 操作类型,1取消,2作废,6核验/发货
	OPERATE_CANCEL(1, "取消"),
	OPERATE_DISCARD(2, "作废"),
	OPEARTE_CHECK_DELIVER(6, "核验/发货");
	
	private int type;
	private String comment;
	
	private OpearteTypeEnum(int type, String comment) {
		this.type = type;
		this.comment = comment;
	}

	public int getType() {
		return type;
	}

	public String getComment() {
		return comment;
	}
	
	public static OpearteTypeEnum parseType(int type) {
		for (OpearteTypeEnum e : OpearteTypeEnum.values()) {
			if (e.type == type) {
				return e;
			}
		}
		return OPERATE_CANCEL;
	}
	
}
