package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkRespDto implements Serializable{
	private static final long serialVersionUID = -6082014497010893549L;
	private long count = 0l;
	private List list = new ArrayList();
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	

}
