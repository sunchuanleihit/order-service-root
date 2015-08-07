package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.List;

public class RefuseReasonListDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4765656616350561143L;
    
    private List<RefuseReasonDto> reasons;

    public RefuseReasonListDto(List<RefuseReasonDto> reasons) {
        super();
        this.reasons = reasons;
    }

    public List<RefuseReasonDto> getReasons() {
        return reasons;
    }

    public void setReasons(List<RefuseReasonDto> reasons) {
        this.reasons = reasons;
    }
}
