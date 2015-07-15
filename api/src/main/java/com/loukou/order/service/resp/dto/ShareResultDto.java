package com.loukou.order.service.resp.dto;

public class ShareResultDto {

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
