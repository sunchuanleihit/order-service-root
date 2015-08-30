package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkCouponListResultDto implements Serializable{
	private static final long serialVersionUID = 2962560802249764923L;
	private int total = 0;
	List<BkCouponListDto> bkCouponList = new ArrayList<BkCouponListDto>();
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<BkCouponListDto> getBkCouponList() {
		return bkCouponList;
	}
	public void setBkCouponList(List<BkCouponListDto> bkCouponList) {
		this.bkCouponList = bkCouponList;
	}
	
	
}
