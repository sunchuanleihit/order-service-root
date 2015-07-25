package com.loukou.order.pay.lib;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.pay.common.PayReqContent;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.pay.weixin.processor.WxPayProcessor;
import com.loukou.pay.weixin.resp.WxUnifiedOrderRespVO;

public class WeiXinPay {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private OrderDao orderDao;
	
	
	// 对订单进行微信支付
	public PayReqContent pay(PayReqContent content) {
		
		//call weixin httpClient, totlfee只能为整数，单位为分
		WxUnifiedOrderRespVO resp = WxPayProcessor.getProcessor().pay(
				content.getOrderSnMain(), (int)(content.getNeedToPay() * 100)
				);
		
		if(StringUtils.equals(resp.getReturnCode(), "SUCCESS")) {
			if(StringUtils.equals(resp.getResultCode(), "SUCCESS")) {
				content.setNeedToPay(0);//支付完成，将还需要支付的金额设置为0
			}
		}
		
		return content;
	}
	
}
