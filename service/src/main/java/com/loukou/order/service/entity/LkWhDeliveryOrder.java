package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lk_wh_delivery_order")
public class LkWhDeliveryOrder {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int   id;
    
    @Column(name = "order_no")
    private String orderNo;
    
    @Column(name = "d_id")
    private int dId;

    @Column(name = "remark")
    private String remark;

    public LkWhDeliveryOrder(){
        super();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Column(name = "order_id")
    private int orderId;
}
