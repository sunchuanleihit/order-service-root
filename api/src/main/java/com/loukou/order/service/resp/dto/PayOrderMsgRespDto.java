package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class PayOrderMsgRespDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5947143299708343351L;
	private PayOrderMsgDto orderMsg = new PayOrderMsgDto();

	public PayOrderMsgDto getOrderMsg() {
		return orderMsg;
	}

	public void setOrderMsg(PayOrderMsgDto orderMsg) {
		this.orderMsg = orderMsg;
	}
	
}
