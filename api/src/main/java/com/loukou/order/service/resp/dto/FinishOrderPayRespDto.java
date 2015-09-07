package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class FinishOrderPayRespDto implements Serializable{
	
	private static final long serialVersionUID = 8720641989147744686L;
	
	/**
	 * 200 － 成功
	 * 404 － 找不到订单
	 * 400 － 订单信息校验失败
	 */
	private int code = 200;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
}
