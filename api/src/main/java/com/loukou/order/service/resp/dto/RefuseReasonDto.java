package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class RefuseReasonDto  implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -5106986898162141236L;

    private int id;
    
    private String reason;
    

    public RefuseReasonDto() {
        super();
    }

    public RefuseReasonDto(int id, String reason) {
        super();
        this.id = id;
        this.reason = reason;
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
