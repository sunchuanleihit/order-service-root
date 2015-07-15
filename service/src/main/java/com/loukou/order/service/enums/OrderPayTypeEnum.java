package com.loukou.order.service.enums;

import org.apache.commons.codec.binary.StringUtils;

public enum OrderPayTypeEnum {

	PAY_APP_ALI("alipay_mobile", "触屏版支付宝支付"),
	PAY_ALI("alipay", "支付宝"),
	PAY_WEB_ALI("loukou_mobile_alipay", "楼口触屏版支付宝支付"),
	PAY_APP_WEIXIN("loukou_app_weixin", "楼口app微信支付"),
	PAY_WEB_WEIXIN("loukou_mobile_weixin", "楼口触屏版微信支付"),
	PAY_WEIXIN("weixin", "微信支付"),
	PAY_VACOUNT("taocz", "虚拟账户"),
	PAY_TAOXINKA("taoxinka", "桃心卡");
	
	private String code;
	private String name;
	
	private OrderPayTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static OrderPayTypeEnum parseType(String code) {
		for (OrderPayTypeEnum e : OrderPayTypeEnum.values()) {
			if (StringUtils.equals(e.code, code)) {
				return e;
			}
		}
		return PAY_ALI;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	
}
