package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class CouponListRespDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -612683492987556890L;
	private int code = 200;
	private String message = "";
	private CouponListResultDto result = new CouponListResultDto();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public CouponListResultDto getResult() {
		return result;
	}

	public void setResult(CouponListResultDto result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
