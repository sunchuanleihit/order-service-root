package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ShareRespDto extends ResponseCodeDto implements Serializable {

	public ShareRespDto(int code, String desc) {
		super(code, desc);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7395161364751922342L;

	private ShareResultDto result = new ShareResultDto();

	public ShareResultDto getResult() {
		return result;
	}

	public void setResult(ShareResultDto result) {
		this.result = result;
	}

}
