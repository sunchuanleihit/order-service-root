package com.loukou.order.service.enums;


public enum PaymentEnum {

//	PAY_APP_ALI("alipay_mobile", "触屏版支付宝支付"),
	PAY_ALI(4, "alipay"),//, "支付宝"
//	PAY_WEB_ALI("loukou_mobile_alipay", "楼口触屏版支付宝支付"),
	PAY_APP_WEIXIN(207, "loukou_app_weixin"),//, "楼口app微信支付"
//	PAY_WEB_WEIXIN("loukou_mobile_weixin", "楼口触屏版微信支付"),
//	PAY_WEIXIN(33, "weixin"),
	PAY_VACOUNT(2, "taocz"),//, "虚拟账户"
	PAY_TXK(6, "taoxinka"),//, "桃心卡"
	PAY_CASH(1, "cash");//,送货上门，看货后付款
	
	private String code;
	private int id;
	
	private PaymentEnum(int id, String code) {
		this.code = code;
		this.id = id;
	}
	
	public static PaymentEnum parseType(int id) {
		for (PaymentEnum e : PaymentEnum.values()) {
			if (e.id == id) {
				return e;
			}
		}
		return PAY_ALI;
	}

	public String getCode() {
		return code;
	}

	public int getId() {
		return id;
	}
	
	
}
