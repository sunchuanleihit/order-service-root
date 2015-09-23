package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoupRuleRespDto extends ResponseCodeDto implements Serializable{
	private static final long serialVersionUID = -3307997415964469893L;
	public CoupRuleRespDto(int code, String message) {
		super(code, message);
	}
	long count = 0l;
	List<CoupRuleDto> coupRuleList = new ArrayList<CoupRuleDto>();
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public List<CoupRuleDto> getCoupRuleList() {
		return coupRuleList;
	}
	public void setCoupRuleList(List<CoupRuleDto> coupRuleList) {
		this.coupRuleList = coupRuleList;
	}
}
