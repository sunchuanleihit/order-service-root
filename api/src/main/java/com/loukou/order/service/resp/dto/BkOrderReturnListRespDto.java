package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkOrderReturnListRespDto extends ResponseCodeDto implements Serializable {
	public BkOrderReturnListRespDto(int code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = -7258996403753865670L;
	
	private BkOrderReturnListDto result = new BkOrderReturnListDto();

	public BkOrderReturnListDto getResult() {
		return result;
	}

	public void setResult(BkOrderReturnListDto result) {
		this.result = result;
	}
}
