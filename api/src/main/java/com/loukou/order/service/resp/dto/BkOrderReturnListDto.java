package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkOrderReturnListDto implements Serializable {
	private static final long serialVersionUID = -7998766797825518548L;
	private List<BkOrderReturnDto> orderReturnList = new ArrayList<BkOrderReturnDto>();
	private int count = 0;
	public List<BkOrderReturnDto> getOrderReturnList() {
		return orderReturnList;
	}
	public void setOrderReturnList(List<BkOrderReturnDto> orderReturnList) {
		this.orderReturnList = orderReturnList;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
