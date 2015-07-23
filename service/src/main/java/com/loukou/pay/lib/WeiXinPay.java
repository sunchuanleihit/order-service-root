package com.loukou.pay.lib;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.loukou.order.service.constants.OrderReqParams;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.req.dto.WeiXinParamsDto;
import com.loukou.order.weixin.pay.processor.WeiXinPayProcessor;
import com.loukou.order.weixin.pay.resp.WeiXinPayRespVO;
import com.loukou.pay.service.common.PayReqContent;

public class WeiXinPay {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private OrderDao orderDao;
	
	
	// 对订单进行微信支付
	public PayReqContent pay(PayReqContent content) {
		WeiXinParamsDto weiXinParamsDto = new WeiXinParamsDto();
		// 计算总额
		double toPay = content.getNeedToPay(); 
		
		weiXinParamsDto.setAppid(OrderReqParams.APPID);
		weiXinParamsDto.setPartnerid(OrderReqParams.MCHID);
		weiXinParamsDto.setPrepayid(getPrepayId());
		weiXinParamsDto.setPackageStr(OrderReqParams.PACKAGE);
		weiXinParamsDto.setNoncestr(createNoncestr(OrderReqParams.NONCESTR_LENGTH));
		weiXinParamsDto.setTimestamp(new Date().getTime() / 1000);
		weiXinParamsDto.setSign(getSign(OrderReqParams.APPID, OrderReqParams.MCHID, 
				getPrepayId(), OrderReqParams.PACKAGE, createNoncestr(OrderReqParams.NONCESTR_LENGTH)));
		weiXinParamsDto.setNeedToPay(toPay);
		
		//call weixin httpClient, totlfee只能为整数，单位为分
		WeiXinPayRespVO resp = WeiXinPayProcessor.getProcessor().pay(content.getOrderSnMain(), (int)(toPay * 100), 
				createNoncestr(OrderReqParams.NONCESTR_LENGTH), 
				getSign(OrderReqParams.APPID, OrderReqParams.MCHID, 
						getPrepayId(), OrderReqParams.PACKAGE, createNoncestr(OrderReqParams.NONCESTR_LENGTH)));
		if(StringUtils.equals(resp.getReturnCode(), "SUCCESS")) {
			if(StringUtils.equals(resp.getResultCode(), "SUCCESS")) {
				content.setNeedToPay(0);//支付完成，将还需要支付的金额设置为0
			}
		}
		
		return content;
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
	
}
