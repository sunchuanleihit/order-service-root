package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkCouponTypeListRespDto extends ResponseCodeDto implements Serializable {

	private static final long serialVersionUID = -2936686365664540039L;

	public BkCouponTypeListRespDto(int code, String message) {
		super(code, message);
	}
	List<BkCouponTypeListDto> bkCouponTypeList = new ArrayList<BkCouponTypeListDto>();

	public List<BkCouponTypeListDto> getBkCouponTypeList() {
		return bkCouponTypeList;
	}
	public void setBkCouponTypeList(List<BkCouponTypeListDto> bkCouponTypeList) {
		this.bkCouponTypeList = bkCouponTypeList;
	}
}
