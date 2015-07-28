package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class CouponListRespDto extends ResponseCodeDto implements Serializable {
	public CouponListRespDto(int code, String message) {
		super(code, message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -612683492987556890L;
	private CouponListResultDto result = new CouponListResultDto();

	public CouponListResultDto getResult() {
		return result;
	}

	public void setResult(CouponListResultDto result) {
		this.result = result;
	}

}
