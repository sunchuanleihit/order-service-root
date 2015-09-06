package com.loukou.order.service.enums;

public enum InviteStatusEnum {
	
	//邀请状态(1:已注册 2:已下单 3:邀请成功4:订单无效)
	//已注册
	INVITESTATUS_REGISTERED(1,"已注册"),
	//已下单
	INVITESTATUS_GOTORDER(2,"已下单"),
	//邀请成功
	 INVITESTATUS_SUCCESS(3,"邀请成功"),
	//已退货
	INVITESTATUS_CANCEL(4,"订单无效");
	
	
	private int id;
	
	private String status;

	InviteStatusEnum(int id, String status) {
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
	
	public static InviteStatusEnum parseType(int id) {
		for (InviteStatusEnum e : InviteStatusEnum.values()) {
			if (e.id == id) {
				return e;
			}
		}
		return INVITESTATUS_REGISTERED;
	}
	
	
}
