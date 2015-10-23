package com.loukou.order.service.enums;

public enum BkOrderStatusEnum {
	STATUS_INIT(0,"初始状态"),
	STATUS_CANCEL(1,"取消"),
	STATUS_INVALID(2,"无效"),
	STATUS_CHECKED(3,"已审核"),
	STATUS_TOKEN(4,"已提货"),
	STATUS_PICKED(5, "已提货"), //已提货
	STATUS_ALLOCATED(6,"已拣货"),
	STATUS_PACKAGED(8,"已打包"),
	STATUS_SHIPPED(13,"已发货"),
	STATUS_DELIVERED(14, "已发货"),// 已发货
	STATUS_RETURNED(15,"回单"),
	STATUS_REFUSED(16,"拒收");
	private int id;
	private String status;
	private BkOrderStatusEnum(int id, String status){
		this.id = id;
		this.status = status;
	}
	public static BkOrderStatusEnum pasrseStatus(int id){
		for(BkOrderStatusEnum e : BkOrderStatusEnum.values()){
			if(e.id == id){
				return e;
			}
		}
		return STATUS_INIT;
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
