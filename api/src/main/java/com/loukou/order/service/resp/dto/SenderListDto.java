package com.loukou.order.service.resp.dto;

import java.util.List;



public class SenderListDto {
    private List<SenderDto> senders;

    public List<SenderDto> getSenders() {
        return senders;
    }

    public void setSenders(List<SenderDto> senders) {
        this.senders = senders;
    }
}
