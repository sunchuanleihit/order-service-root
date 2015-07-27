package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.Date;

public class DeliveryInfo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4756721764266897267L;
    private Date time; // 配送时间
    private String address; // 配送地址　　区域＋地址
    private String consignee; // 收货人
    private String tel;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "DeliveryInfo [time=" + time + ", address=" + address + ", consignee=" + consignee + ", tel=" + tel
                + "]";
    }
}
