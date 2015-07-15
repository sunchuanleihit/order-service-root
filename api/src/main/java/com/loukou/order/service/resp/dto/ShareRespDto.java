package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ShareRespDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7395161364751922342L;

	private int code = 200;

	private ShareResultDto shareResultDto;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ShareResultDto getShareResultDto() {
		return shareResultDto;
	}

	public void setShareResultDto(ShareResultDto shareResultDto) {
		this.shareResultDto = shareResultDto;
	}

}
