package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayBeforeResultDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3731552768063373025L;
	
	private PayOrderMsgDto orderMsg = new PayOrderMsgDto();

	public PayOrderMsgDto getOrderMsg() {
		return orderMsg;
	}

	public void setOrderMsg(PayOrderMsgDto orderMsg) {
		this.orderMsg = orderMsg;
	}

}
