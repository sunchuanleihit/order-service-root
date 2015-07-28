package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.List;

public class OrderListResultDto implements Serializable {

	private static final long serialVersionUID = -1434301276931075935L;

	private List<OrderListDto> orderList;
	private int orderCount;

	public List<OrderListDto> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderListDto> orderList) {
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
