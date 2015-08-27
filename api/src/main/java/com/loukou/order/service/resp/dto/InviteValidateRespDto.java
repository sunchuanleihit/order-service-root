package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.List;

public class InviteValidateRespDto implements Serializable {


	/**
	 * 邀请码验证结果实体
	 */
	private static final long serialVersionUID = 8868040713788128720L;
	//金额
	private double  money; 
	//消息编码 0开头成功
	private int code;
	//消息
	private String message;
	//手机号
	private String phoneNumber;
	

	

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
 
}
