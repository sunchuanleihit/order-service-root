package com.loukou.order.service.enums;

public enum BkOrderSourceEnum {
	//查线上数据库最近两个月的订单来源
	SOURCE_NONE(-1, ""),
	SOURCE_WEB(0, "网站"),
	SOURCE_CMCC(1, "移动公司"),
	SOURCE_ANDROID(21, "android"),//ANDROID
	SOURCE_IOS(30, "ios"),//IOS
	SOURCE_LARGE(35, "大宗触屏版下单"),
	SOURCE_CVS(50, "便利店内下单"),
	SOURCE_WC(51, "微仓"),
	SOURCE_DT(52, "地推");
	private int id;
	private String source;
	
	private BkOrderSourceEnum(int id, String source) {
		this.id = id;
		this.source = source;
	}

	public int getId() {
		return id;
	}

	public String getSource() {
		return source;
	}
	
	public static BkOrderSourceEnum parseSource(int id) {
		for (BkOrderSourceEnum e : BkOrderSourceEnum.values()) {
			if (e.id == id) {
				return e;
			}
		}
		return SOURCE_NONE;
	}
}
