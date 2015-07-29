package com.loukou.order.service.enums;

public enum WeiCangGoodsStoreStatusEnum {
	STATUS_OFFSHELVES(0),
	
	STATUS_ONSHELVES(1);
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private WeiCangGoodsStoreStatusEnum(int id) {
		this.id = id;
	}
	
	
}
