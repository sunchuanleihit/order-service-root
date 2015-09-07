package com.loukou.order.service.req.dto;

import java.io.Serializable;

public class InviteValidateReqDto  implements Serializable {

	/**
	 * 邀请码验证获取优惠券实体
	 */
	private static final long serialVersionUID = 376521614963579522L;

	
	//邀请码
	private String inviteCode;
	
	//验证码
	private String validateCode;
	
	//微信ID
	private String winXinId;
	
	//手机号
	private String phoneNumber;



	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getWinXinId() {
		return winXinId;
	}

	public void setWinXinId(String winXinId) {
		this.winXinId = winXinId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
