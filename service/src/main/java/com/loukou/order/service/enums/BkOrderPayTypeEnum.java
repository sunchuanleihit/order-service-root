package com.loukou.order.service.enums;

public enum BkOrderPayTypeEnum {
	NOPAYMENT(0,"未选支付方式"),
	CASH(1,"现金"),
	TAOCZ(2,"虚拟账户"),
	UNION(3,"虚拟账户"),
	ALIPAY(4,"支付宝"),
	TENPAY(5,"财付通"),
	TAOXINKA(6,"淘心卡"),
	POS(7,"pos机"),
	JIANHANG(8,"建行支行"),
	SH_UNION(9,"上海银联无卡支付"),
	CHEQUE(10,"支票"),
	AGRI(11,"农行支行"),
	SYT(12,"收银台付款"),
	TXB(13,"积分"),
	YHQ(14,"优惠券"),
	YDB(15,"移动商城币"),
	BOC(16,"中行支行"),
	JNBANK(19,"江南银行网银支行"),
	BOCOM(20,"交行支付"),
	JNBANKJF(21,"江南银行积分支付"),
	ZHSQ(22,"智慧社区支付"),
	CNCB(23,"中信银行支付"),
	ICBC(24,"工商银行支付"),
	BON(25,"南京银行"),
	CEB(27,"广大银行"),
	BOCPLAN(28,"中信信用卡支付"),
	MONSUM(31,"月结"),
	PSBC(32,"邮政储蓄"),
	WEIXIN(33,"微信支付"),
	CMBC(37,"民生银行"),
	CMB(38,"招商银行"),
	ACTIVITY(100,"活动"),
	SMK(150,"市民卡支付"),
	ALIPAY_MOBILE(151,"触屏版支付宝支付"),
	LOUKOU_MOBILE_ALIPAY(200, "楼口触屏版支付宝支付"),
	LOUKOU_MOBILE_WEIXIN(201,"楼口触屏版微信支付"),
	LOUKOU_MOBILE_ZS(202, "楼口触屏版招商支付"),
	LOUKOU_MOBILE_ZH(203,"楼口触屏版中行支付"),
	LOUKOU_MOBILE_NH(204,"楼口触屏版农行支付"),
	LOUKOU_MOBILE_JH(205,"楼口触屏版建行支付"),
	LOUKOU_MOBILE_MS(206,"楼口触屏版民生支付"),
	LOUKOU_APP_WEIXIN(207,"楼口APP微信支付"),
	CMBC_NEW(208,"新版民生支付");
	
	private int id;
	private String payType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	private BkOrderPayTypeEnum(int id, String payType) {
		this.id = id;
		this.payType = payType;
	}
	
	public static BkOrderPayTypeEnum parseType(int id) {
		for (BkOrderPayTypeEnum e : BkOrderPayTypeEnum.values()) {
			if (e.id == id) {
				return e;
			}
		}
		return NOPAYMENT;
	}
}
