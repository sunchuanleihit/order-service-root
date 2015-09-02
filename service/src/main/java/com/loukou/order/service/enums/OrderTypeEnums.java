package com.loukou.order.service.enums;

import org.apache.commons.codec.binary.StringUtils;

public enum OrderTypeEnums {

	TYPE_MATERIAL("material", "普通商品"),
	TYPE_BOOKING("booking", "预购订单"),
	TYPE_SELF_SALES("self_sales", "商家独立销售"),
	TYPE_WEI_SELF("wei_self", "微仓订单(独立进货)"),
	TYPE_WEI_WH("wei_wh", "微仓订单(总仓进货)");
	
	private String type;
	private String comment;
	
	private OrderTypeEnums(String type, String comment) {
		this.type = type;
		this.comment = comment;
	}
	
	public static OrderTypeEnums parseType(String type) {
		for (OrderTypeEnums e : OrderTypeEnums.values()) {
			if (StringUtils.equals(e.type, type)) {
				return e;
			}
		}
		return TYPE_MATERIAL;
	}

	public String getType() {
		return type;
	}

	public String getComment() {
		return comment;
	}
	
	
}
