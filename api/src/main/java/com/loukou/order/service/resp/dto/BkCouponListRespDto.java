package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkCouponListRespDto extends ResponseCodeDto implements Serializable {

	private static final long serialVersionUID = -2936686365664540039L;

	public BkCouponListRespDto(int code, String message) {
		super(code, message);
	}
	private BkCouponListResultDto result = new BkCouponListResultDto();

	public BkCouponListResultDto getResult() {
		return result;
	}

	public void setResult(BkCouponListResultDto result) {
		this.result = result;
	}
}
