package com.loukou.order.service.enums;


public enum OrderStatusEnum {

	STATUS_UNKNOWN(-1),
	
	STATUS_NEW(0),	// 初始状态
	
	STATUS_CANCELED(1),	// 取消
	
	STATUS_INVALID(2),	// 无效
	
	STATUS_REVIEWED(3), // 已审核

	STATUS_ALLOCATED(6), // 已拣货

	STATUS_PACKAGED(8), // 打包
	
	STATUS_DELIVERIED(13),	// 已发货
	
	STATUS_FINISHED(15); // 回单
	
	private int id;
	
	private OrderStatusEnum(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
