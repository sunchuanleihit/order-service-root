package com.loukou.order.service.resp.dto;

public class SenderDto {
    private int senderId;
    private String senderName;
    public int getSenderId() {
        return senderId;
    }
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    @Override
    public String toString() {
        return "SendDto [senderId=" + senderId + ", senderName=" + senderName + "]";
    }
}
