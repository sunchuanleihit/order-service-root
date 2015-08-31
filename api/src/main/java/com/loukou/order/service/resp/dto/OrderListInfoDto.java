package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderListInfoDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7482849533226888468L;

    private int storeId;

    private long totalNum;

    private int pageNum;

    private List<OrderInfoDto> orders = new ArrayList<OrderInfoDto>();

    public List<OrderInfoDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderInfoDto> orders) {
        this.orders = orders;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "OrderListInfoDto [storeId=" + storeId + ", totalNum=" + totalNum + ", orders=" + orders + "]";
    }
}
