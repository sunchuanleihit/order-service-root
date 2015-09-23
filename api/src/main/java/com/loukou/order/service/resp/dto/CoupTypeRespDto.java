package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoupTypeRespDto extends ResponseCodeDto implements Serializable{
	private static final long serialVersionUID = 3147693122158781024L;

	public CoupTypeRespDto(int code, String message) {
		super(code, message);
	}
	
	List<BkCouponTypeListDto> coupTypeList = new ArrayList<BkCouponTypeListDto>();
	long count = 0l;

	
	public List<BkCouponTypeListDto> getCoupTypeList() {
		return coupTypeList;
	}
	public void setCoupTypeList(List<BkCouponTypeListDto> coupTypeList) {
		this.coupTypeList = coupTypeList;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	
}
