package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkCouponTypeListRespDto extends ResponseCodeDto implements Serializable {

	private static final long serialVersionUID = -2936686365664540039L;

	public BkCouponTypeListRespDto(int code, String message) {
		super(code, message);
	}
	long count = 0l;
	List<BkCouponTypeListDto> bkCouponTypeList = new ArrayList<BkCouponTypeListDto>();

	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public List<BkCouponTypeListDto> getBkCouponTypeList() {
		return bkCouponTypeList;
	}
	public void setBkCouponTypeList(List<BkCouponTypeListDto> bkCouponTypeList) {
		this.bkCouponTypeList = bkCouponTypeList;
	}
}
