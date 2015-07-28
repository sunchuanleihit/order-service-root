package com.loukou.order.service.enums;


public enum FinishPayResultEnum {

	RESULT_UNKNOWN(0, "UNKNOWN"),
	
	RESULT_SUCC(1, "succ"),

	RESULT_ORDER_NOTFOUND(2, "order not found"),
	
	RESULT_VERIFY_FAILED(3, "verify failed"),

	RESULT_UNSUPPORT_PAYMENT(4, "unsupport payment"),
	
	RESULT_INTERNAL_ERROR(5, "internal error"); 
	
	private int id;
	private String result;
	
	private FinishPayResultEnum(int id, String result) {
		this.id = id;
		this.result = result;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
