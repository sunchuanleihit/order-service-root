package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.List;

public class OrderInfoDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4340360531877963545L;
    private String orderNo;// 订单包裹号

    private int orderStatus;

    private double orderAmount;

    private double shippingFee;

    private String addTime;

    private List<GoodsListDto> goodsListDtos;

    private ExtmMsgDto extmMsgDto;

   
    public String getOrderNo() {
        return orderNo;
    }


    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    public int getOrderStatus() {
        return orderStatus;
    }


    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }


    public double getOrderAmount() {
        return orderAmount;
    }


    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }


    public double getShippingFee() {
        return shippingFee;
    }


    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }


    public String getAddTime() {
        return addTime;
    }


    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }


    public List<GoodsListDto> getGoodsListDtos() {
        return goodsListDtos;
    }


    public void setGoodsListDtos(List<GoodsListDto> goodsListDtos) {
        this.goodsListDtos = goodsListDtos;
    }


    public ExtmMsgDto getExtmMsgDto() {
        return extmMsgDto;
    }


    public void setExtmMsgDto(ExtmMsgDto extmMsgDto) {
        this.extmMsgDto = extmMsgDto;
    }


    @Override
    public String toString() {
        return "OrderInfoDto [orderNo=" + orderNo + ", orderStatus=" + orderStatus + ", orderAmount=" + orderAmount
                + ", shippingFee=" + shippingFee + ", addTime=" + addTime + ", goodsListDtos=" + goodsListDtos
                + ", extmMsgDto=" + extmMsgDto + "]";
    }

}
