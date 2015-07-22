package com.loukou.order.service.constants;

public class OrderReqParams {

	public final static String NOTIFY_URL = "http://newapi.loukou.com/paynotify-material_loukou_app_weixin.html";
	public final static String APPID = "wxec40bee1f3b6e332";//返回APP参数数组
	public final static String MCHID = "123581910";//受理商ID，身份标识
	//商户支付密钥Key。审核通过后，在微信发送的邮件中查看
	public final static String KEY = "18a6fb1d854582e5054b445c7057f5e0";
	//JSAPI接口中获取openid，审核后在公众平台开启开发模式后可查看
	public final static String APPSECRET = "5300cf1dd55235ccaeab03c44b8453c8";
	
	public final static String PACKAGE = "Sign=WXPay";
	
	public final static String WEIXIN_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public final static int NONCESTR_LENGTH = 32;
	
}
