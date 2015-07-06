package com.loukou.order.service.resp.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainOrderListDto {

	private Map<String, List<OrderListDto>> mainOrderList = new HashMap<String, List<OrderListDto>>();

	public Map<String, List<OrderListDto>> getMainOrderList() {
		return mainOrderList;
	}

	public void setMainOrderList(Map<String, List<OrderListDto>> mainOrderList) {
		this.mainOrderList = mainOrderList;
	}
	
	
}
