package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class LkWhDelivery {
    @Id
    @GeneratedValue
    @Column(name = "d_id")
    private int dId;

    @Column(name = "d_name")
    private String dName;

    @Column(name = "d_mobile")
    private String dMobile;

    @Column(name = "d_address")
    private String dAddress;

    @Column(name = "deleted")
    private String deleted;

    @Column(name = "password")
    private String password;

    public LkWhDelivery(){
        super();
    }
    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdMobile() {
        return dMobile;
    }

    public void setdMobile(String dMobile) {
        this.dMobile = dMobile;
    }

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
