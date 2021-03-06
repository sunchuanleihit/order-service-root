package com.loukou.order.service.enums;


public enum OrderStatusEnum {

	STATUS_UNKNOWN(-1),
	
	STATUS_NEW(0),	// 初始状态
	
	STATUS_CANCELED(1),	// 取消
	
	STATUS_INVALID(2),	// 无效 微仓拒绝订单
	
	STATUS_REVIEWED(3), // 已审核
	
	STATUS_PICKED(5), //已提货

	STATUS_ALLOCATED(6), // 已拣货

	STATUS_PACKAGED(8), // 打包
	
	STATUS_DELIVERIED(13),	// 已发货
	
	STATUS_14(14),// 已发货
	
	STATUS_FINISHED(15), // 回单
	
	STATUS_REFUSED(16); //拒收订单 这是送货后出现的问题
	
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
