package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ShareResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -280100977315243934L;
	private String image;
	private String arrivalCode;

	private String desc;
	private ShareDto share;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getArrivalCode() {
		return arrivalCode;
	}

	public void setArrivalCode(String arrivalCode) {
		this.arrivalCode = arrivalCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ShareDto getShare() {
		return share;
	}

	public void setShare(ShareDto share) {
		this.share = share;
	}

}
