package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoupTypeRespDto extends ResponseCodeDto implements Serializable{
	private static final long serialVersionUID = 3147693122158781024L;

	public CoupTypeRespDto(int code, String message) {
		super(code, message);
	}
	
	List<CoupTypeDto> coupTypeList = new ArrayList<CoupTypeDto>();
	long count = 0l;

	public List<CoupTypeDto> getCoupTypeList() {
		return coupTypeList;
	}
	public void setCoupTypeList(List<CoupTypeDto> coupTypeList) {
		this.coupTypeList = coupTypeList;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	
}
