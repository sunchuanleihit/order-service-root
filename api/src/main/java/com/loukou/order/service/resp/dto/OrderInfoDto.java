package com.loukou.order.service.resp.dto;

import java.io.Serializable;
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
    private int isBooking; //是否时预售  0表示非预售　1表示预售
    private String createTime; // 下单时间
    private String needShippingTime; //需要送达时间
    private String rejectTime; // 拒绝时间
    private String rejectReason; // 拒绝原因
    private String cancelTime; // 取消时间
    private String finishTime; // 回单时间
    private int orderStatus; // 订单状态
    private String  goodsReturnStatus; // 待退货　已退货　
    private DeliveryInfo deliveryInfo; // 配送信息
    private List<SpecDto> specList; // 规格信息列表
    private int deliveryResult;//送达结果　　　早，及时，延时

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
    public String getRejectTime() {
        return rejectTime;
    }
    public void setRejectTime(String rejectTime) {
        this.rejectTime = rejectTime;
    }
    public String getRejectReason() {
        return rejectReason;
    }
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
    public String getCancelTime() {
        return cancelTime;
    }
    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }
    public String getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
    public int getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getGoodsReturnStatus() {
        return goodsReturnStatus;
    }
    public void setGoodsReturnStatus(String goodsReturnStatus) {
        this.goodsReturnStatus = goodsReturnStatus;
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
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
    public String getNeedShippingTime() {
        return needShippingTime;
    }
    public void setNeedShippingTime(String needShippingTime) {
        this.needShippingTime = needShippingTime;
    }
    public int getIsBooking() {
        return isBooking;
    }
    public void setIsBooking(int isBooking) {
        this.isBooking = isBooking;
    }
    public int getDeliveryResult() {
        return deliveryResult;
    }
    public void setDeliverResult(int deliverResult) {
        this.deliveryResult = deliverResult;
    }
  
}
