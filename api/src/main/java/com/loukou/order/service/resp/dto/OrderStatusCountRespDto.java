package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class OrderStatusCountRespDto implements Serializable {
        /**
     * 
     */
    private static final long serialVersionUID = -6922792775585869005L;

        public int getOrderStatusReviewed() {
        return orderStatusReviewed;
    }

    public void setOrderStatusReviewed(int orderStatusReviewed) {
        this.orderStatusReviewed = orderStatusReviewed;
    }

    public int getOrderStatusBookingRecieve() {
        return orderStatusBookingRecieve;
    }

    public void setOrderStatusBookingRecieve(int orderStatusBookingRecieve) {
        this.orderStatusBookingRecieve = orderStatusBookingRecieve;
    }

    //审核通过的订单 不包含预售
        private int orderStatusReviewed;
        
        //预售到货
        private int orderStatusBookingRecieve;
}
