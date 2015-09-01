package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkOrderRemarkListRespDto extends ResponseCodeDto  implements Serializable{
	public BkOrderRemarkListRespDto(int code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = -8996057733891543511L;

	private int total = 0;
	
	private List<BkOrderRemarkDto> bkOrderRemarkList = new ArrayList<BkOrderRemarkDto>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<BkOrderRemarkDto> getBkOrderRemarkList() {
		return bkOrderRemarkList;
	}

	public void setBkOrderRemarkList(List<BkOrderRemarkDto> bkOrderRemarkList) {
		this.bkOrderRemarkList = bkOrderRemarkList;
	}
	
}
