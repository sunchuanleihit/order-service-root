package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkTxkRecordListRespDto extends ResponseCodeDto implements Serializable{
	private static final long serialVersionUID = 6685997912453110272L;

	public BkTxkRecordListRespDto(int code, String message) {
		super(code, message);
	}
	
	List<BkTxkRecordDto> recordList = new ArrayList<BkTxkRecordDto>();
	private Integer total = 0;

	public List<BkTxkRecordDto> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<BkTxkRecordDto> recordList) {
		this.recordList = recordList;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
	
}
