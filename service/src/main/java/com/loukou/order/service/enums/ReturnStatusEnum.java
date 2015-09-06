package com.loukou.order.service.enums;


public enum ReturnStatusEnum {

	STATUS_NOT_VERIFY(0, "未审核"), 
	
	STATUS_CANCELED(1, "已取消"), 
	
	STATUS_INVALID(2, "无效"), 
	
	STATUS_VERIFIED(3, "已审核"), 
	
	STATUS_PICKED(5, "已提货"), 
	
	STATUS_ALLOCATED(6, "已拣货"), 
	
	STATUS_PACKAGED(8, "打包"), 
	
	STATUS_DELIVERIED(13, "已发货"), 
	
	STATUS_14(14, "已发货"),
	
	STATUS_RECIVED(15, "已收货"), 
	
	STATUS_REJECT(16, "拒收"), 
	
	STATUS_FINISHED(15, "回单"); // 回单

	private int status;
	private String comment;

	private ReturnStatusEnum(int status, String comment) {
		this.status = status;
		this.comment = comment;
	}

	public int getStatus() {
		return status;
	}

	public String getComment() {
		return comment;
	}
	
	public static ReturnStatusEnum parseType(int status) {
		for (ReturnStatusEnum e : ReturnStatusEnum.values()) {
			if (e.status == status) {
				return e;
			}
		}
		return STATUS_NOT_VERIFY;
	}

}
