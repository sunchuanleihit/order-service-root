package com.loukou.order.service.req.dto;

public class OrderListParamDto {

    private int pageSize;
    private int pageNum;
    private int storeId;
    private int orderStatus;
    private int orderType;
    
    
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getStoreId() {
        return storeId;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public int getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
    @Override
    public String toString() {
        return "OrderListParamDto [pageSize=" + pageSize + ", pageNum=" + pageNum + ", storeId=" + storeId
                + ", orderStatus=" + orderStatus + "]";
    }
    public int getOrderType() {
        return orderType;
    }
    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
    
}
