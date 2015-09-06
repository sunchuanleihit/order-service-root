package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lk_order_refuse")
public class OrderRefuse {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "tao_order_sn")
    private String taoOrderSn;

    @Column(name = "refuse_id")
    private int refuseId;

    @Column(name = "refuse_reason")
    private String refuseReason;

    @Column(name = "refuse_time")
    private Date refuseTime;

    public OrderRefuse() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaoOrderSn() {
        return taoOrderSn;
    }

    public void setTaoOrderSn(String taoOrderSn) {
        this.taoOrderSn = taoOrderSn;
    }

    public int getRefuseId() {
        return refuseId;
    }

    public void setRefuseId(int refuseId) {
        this.refuseId = refuseId;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Date getRefuseTime() {
        return refuseTime;
    }

    public void setRefuseTime(Date refuseTime) {
        this.refuseTime = refuseTime;
    }
}
