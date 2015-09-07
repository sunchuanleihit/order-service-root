package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PaymentResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3342519396294182345L;
	private String error;
	private String msg;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
