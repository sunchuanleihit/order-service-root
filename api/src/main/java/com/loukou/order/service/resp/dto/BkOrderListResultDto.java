package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkOrderListResultDto implements Serializable {

	private static final long serialVersionUID = -1434301276931075935L;

	private List<BkOrderListDto> orderList = new ArrayList<BkOrderListDto>();
	private int orderCount = 0;

	public List<BkOrderListDto> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<BkOrderListDto> orderList) {
		this.orderList = orderList;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

    @Override
    public String toString() {
        return "OrderListResultDto [orderList=" + orderList + ", orderCount=" + orderCount + "]";
    }
	

}
