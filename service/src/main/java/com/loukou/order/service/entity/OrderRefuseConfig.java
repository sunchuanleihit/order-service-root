package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="lk_order_refuse_config")
public class OrderRefuseConfig {
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;
    
    @Column(name="reason")
    private String reason;
    public OrderRefuseConfig(){
        super();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}
