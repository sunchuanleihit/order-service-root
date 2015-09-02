package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainOrderListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5060340018253273164L;
	private Map<String, List<OrderListDto>> mainOrderList = new HashMap<String, List<OrderListDto>>();

	public Map<String, List<OrderListDto>> getMainOrderList() {
		return mainOrderList;
	}

	public void setMainOrderList(Map<String, List<OrderListDto>> mainOrderList) {
		this.mainOrderList = mainOrderList;
	}
	
	
}
