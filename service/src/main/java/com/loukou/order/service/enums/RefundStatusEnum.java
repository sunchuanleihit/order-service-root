package com.loukou.order.service.enums;

//退款状态
public enum RefundStatusEnum {

	/**
     * 返回退款状态 flag 1:全部 2:待付款 3:待收货 4:退货
     */
	STATUS_ALL(1), 
	STATUS_UNPAIED(2),
	STATUS_UNRECIVE(3),
	STATUS_RETURNED(4);

	
	private RefundStatusEnum(int flag) {
		this.flag = flag;
	}
	
	private int flag;

	public int getFlag() {
		return flag;
	}
	
	public static RefundStatusEnum parseStatus(int flag) {
		for (RefundStatusEnum e : RefundStatusEnum.values()) {
			if (e.flag == flag) {
				return e;
			}
		}
		return STATUS_ALL;
	}
}
