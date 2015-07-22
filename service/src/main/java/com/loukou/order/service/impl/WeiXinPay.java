package com.loukou.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.loukou.order.service.constants.OrderReqParams;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.req.dto.WeiXinParamsDto;
import com.loukou.order.service.req.dto.XmlParamsDto;
import com.loukou.order.service.util.DoubleUtils;

public class WeiXinPay {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private OrderDao orderDao;
	
	
	// 对订单进行微信支付
	public boolean pay(String orderSnMain, List<OrderModels> allModels) {
		boolean needToPay = false;
		WeiXinParamsDto weiXinParamsDto = new WeiXinParamsDto();
		// 计算总额
		double toPay = 0; 
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		for(Order order : orders) {
		//	if(order.getPayId() == PaymentEnum.PAY_VACOUNT.getId()) {
				toPay = DoubleUtils.add(order.getGoodsAmount(), toPay);
				toPay = DoubleUtils.add(toPay, order.getShippingFee());
		//	}
		}
		double payedMoney = 0;
		for (OrderModels model : allModels) {
			for(OrderPay pay : model.getPays()) {
				if(StringUtils.equals(pay.getStatus(), OrderPayStatusEnum.STATUS_SUCC.getStatus())) {
					payedMoney = DoubleUtils.add(pay.getMoney(), toPay);
				}
			}
		}
		toPay = DoubleUtils.sub(toPay, payedMoney);
		if (toPay == 0) {
			needToPay = false;
			return needToPay;
		}
		weiXinParamsDto.setAppid(OrderReqParams.APPID);
		weiXinParamsDto.setPartnerid(OrderReqParams.MCHID);
		weiXinParamsDto.setPrepayid(getPrepayId());
		weiXinParamsDto.setPackageStr(OrderReqParams.PACKAGE);
		weiXinParamsDto.setNoncestr(createNoncestr(OrderReqParams.NONCESTR_LENGTH));
		weiXinParamsDto.setTimestamp(new Date().getTime() / 1000);
		weiXinParamsDto.setSign(getSign(OrderReqParams.APPID, OrderReqParams.MCHID, 
				getPrepayId(), OrderReqParams.PACKAGE, createNoncestr(OrderReqParams.NONCESTR_LENGTH)));
		weiXinParamsDto.setNeedToPay(toPay);
		
		//TODO call weixin httpClient
		return needToPay;
	}
	
	private String getSign(String appid, String partnerid, String prepayid, String packageStr, String noncestr) {
		StringBuilder result = new StringBuilder();
		result.append("appid=").append(appid).append("&")
		.append("partnerid=").append(partnerid).append("&")
		.append("prepayid=").append(prepayid).append("&")
		.append("package=").append(packageStr).append("&")
		.append("noncestr").append(noncestr).append("&")
		.append("key=").append(OrderReqParams.KEY);
		return StringUtils.upperCase(DigestUtils.md5DigestAsHex(result.toString().getBytes()));
	}
	
	private String getPrepayId() {
		
		return "";
	}
	
	/**
	 * 	作用：产生随机字符串，不长于32位(32位)
	 */
	private String createNoncestr(int length) {
		return RandomStringUtils.random(length, "abcdefghijklmnopqrstuvwxyz0123456789");
	}
	
	
	private String  xml(XmlParamsDto xmlDto) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>");
		xml.append("<").append("appid").append("><![CDATA[").append(xmlDto.getAppid()).append("]]></")
			.append("appid").append(">");
		xml.append("<").append("body").append("><![CDATA[").append(xmlDto.getBody()).append("]]></")
		.append("body").append(">");
		xml.append("<").append("mchid").append("><![CDATA[").append(xmlDto.getMchId()).append("]]></")
		.append("mchid").append(">");
		xml.append("<").append("noncestr").append("><![CDATA[").append(xmlDto.getNonceStr()).append("]]></")
		.append("noncestr").append(">");
		xml.append("<").append("notifyUrl").append("><![CDATA[").append(xmlDto.getNotifyUrl()).append("]]></")
		.append("notifyUrl").append(">");
		xml.append("<").append("outTradeNo").append("><![CDATA[").append(xmlDto.getOutTradeNo()).append("]]></")
		.append("outTradeNo").append(">");
		xml.append("<").append("sign").append("><![CDATA[").append(xmlDto.getSign()).append("]]></")
		.append("sign").append(">");
		xml.append("<").append("tradeType").append("><![CDATA[").append(xmlDto.getTradeType()).append("]]></")
		.append("tradeType").append(">");
		xml.append("<").append("totalFee").append(">").append(xmlDto.getTotalFee()).append("</")
		.append("totalFee").append(">");
		
		xml.append("</xml>");
		
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		StringEntity xmlEntity = new StringEntity(xml.toString(), 
//				   ContentType.create("text/plain", "UTF-8"));
//		
//		HttpPost httpPost = new HttpPost(OrderReqParamsWEIXIN_PAY_URL);
//		
		return xml.toString();
	}
	
	
}
