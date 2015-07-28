package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderInfoDto  implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -2137458056530086065L;
    private String taoOrderSn; // 包裹单号
    private Double goodsAmount; // 商品总金额
    private Double shippingFee; // 运费
    private String shippingNo; // 预售单物流单号
    private String createTime; // 下单时间
    private Date rejectTime; // 拒绝时间
    private String rejectReason; // 拒绝原因
    private Date cancelTime; // 取消时间
    private Date finishTime; // 回单时间
    private int orderStatus; // 订单状态
    private int goodsReturnStatus; // 待退货　已退货　
    private int moneyReturnStatus; // 退款中　退款成功　退款失败
    private DeliveryInfo deliveryInfo; // 配送信息
    private List<SpecDto> specList; // 规格信息列表

    public String getTaoOrderSn() {
        return taoOrderSn;
    }

    public void setTaoOrderSn(String taoOrderSn) {
        this.taoOrderSn = taoOrderSn;
    }

    public Double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Date getRejectTime() {
        return rejectTime;
    }

    public void setRejectTime(Date rejectTime) {
        this.rejectTime = rejectTime;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getGoodsReturnStatus() {
        return goodsReturnStatus;
    }

    public void setGoodsReturnStatus(int goodsReturnStatus) {
        this.goodsReturnStatus = goodsReturnStatus;
    }

    public int getMoneyReturnStatus() {
        return moneyReturnStatus;
    }

    public void setMoneyReturnStatus(int moneyReturnStatus) {
        this.moneyReturnStatus = moneyReturnStatus;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public List<SpecDto> getSpecList() {
        return specList;
    }

    public void setSpecList(List<SpecDto> specList) {
        this.specList = specList;
    }

    @Override
    public String toString() {
        return "UserOrderDto [taoOrderSn=" + taoOrderSn + ", goodsAmount=" + goodsAmount + ", shippingFee="
                + shippingFee + ", shippingNo=" + shippingNo + ", createTime=" + createTime + ", rejectTime="
                + rejectTime + ", rejectReason=" + rejectReason + ", cancelTime=" + cancelTime + ", finishTime="
                + finishTime + ", orderStatus=" + orderStatus + ", goodsReturnStatus=" + goodsReturnStatus
                + ", moneyReturnStatus=" + moneyReturnStatus + ", deliveryInfo=" + deliveryInfo + ", specList="
                + specList + "]";
    }

}
