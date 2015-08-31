package com.loukou.order.service.enums;


public enum ActivateCouponMessage {
	
	ERROR_COMMONCODE(0, "优惠码错误"),
	
	SUCCESS(1, ""),
	
	OVER_DUE(2, "优惠码已过期'"),
	
	USED(3, "已使用过该优惠"),
	
	INVALID(4, "优惠码无效"),
	
	FINISHED(5, "该优惠已被领完"),
	
	NEW_USER_ONLY(6, "该优惠仅限新用户"),
	
	OLD_USER_ONLY(7, "该优惠仅限老用户"),
	
	MUTEX(8, "已使用另一优惠"),
	
	MACHINE_USED(9, "您的设备已使用过该优惠");
	
	ActivateCouponMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static ActivateCouponMessage parseCode(int code) {
		for (ActivateCouponMessage e : ActivateCouponMessage.values()) {
			if (e.code == code) {
				return e;
			}
		}
		return ERROR_COMMONCODE;
	}
	
	private int code;
	
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
}
