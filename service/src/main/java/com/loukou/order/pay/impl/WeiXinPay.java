package com.loukou.order.pay.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.loukou.order.pay.dal.BasePayContext;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.resp.dto.WeixinPayOrderResultDto;
import com.loukou.pay.weixin.constant.WxPayConstant;
import com.loukou.pay.weixin.processor.WxPayProcessor;
import com.loukou.pay.weixin.resp.WxUnifiedOrderRespVO;
import com.loukou.pay.weixin.util.WxPayUtil;

@Service
public class WeiXinPay {

	private final Logger logger = Logger.getLogger(WeiXinPay.class);

	// 对订单进行微信支付准备
	public WeixinPayOrderResultDto preparePay(BasePayContext context) {

		WeixinPayOrderResultDto result = new WeixinPayOrderResultDto();

		double needToPay = context.getAmountToPay();
		// 构造外部支付订单
		String outTradeNo = context.makeOutTrade(context.getUserId(),
				context.getOrderSnMain(), needToPay,
				PaymentEnum.PAY_APP_WEIXIN.getId());
		if (StringUtils.isBlank(outTradeNo)) {
			// 生成外部支付订单失败，滚粗
			// TODO
			logger.error(String
					.format("preparePay weixin fail to make out trade user_id[%d] order_sn_main[%s]",
							context.getUserId(), context.getOrderSnMain()));
			return null;
		} else {
			logger.info(String
					.format("preparePay weixin done to make out trade user_id[%d] order_sn_main[%s] out_trade_no[%s]",
							context.getUserId(), context.getOrderSnMain(),
							outTradeNo));
		}
		if(needToPay > 0.0)
		{
			// 调用微信统一支付接口
			String goodsName = String.format("%s", context.getOrderSnMain());
			// !!!! 注意⚠这里外部交易号用的是主单号 跟支付宝不一样 !!!!
			WxUnifiedOrderRespVO unifiedOrderResp = WxPayProcessor.getProcessor()
					.pay(context.getOrderSnMain(), goodsName, needToPay);
			if (!WxPayProcessor.isSuccessUnifiedOrder(unifiedOrderResp)) {
				logger.error(String
						.format("preparePay weixin fail to make unified order user_id[%d] order_sn_main[%s] out_trade_no[%s]",
								context.getUserId(), context.getOrderSnMain(),
								outTradeNo));
				// 调用失败，滚粗
				// TODO
				return null;
			} else {
				logger.info(String
						.format("preparePay weixin done to make unified order user_id[%d] order_sn_main[%s] out_trade_no[%s]",
								context.getUserId(), context.getOrderSnMain(),
								outTradeNo));
			}
			result.setPrepayId(unifiedOrderResp.getPrepayId());
			result.setNonceStr(unifiedOrderResp.getNoncestr());
		}
		// 构造收银台结构
		result.setAppId(WxPayConstant.APPID);
		result.setPartner(WxPayConstant.MCHID);
		result.setPackageStr(WxPayConstant.PACKAGESTR);
		result.setTimeStamp((int)(new Date().getTime() / 1000));
		result.setNeedPay(needToPay);
		// 签名
		// https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=9_12&index=2
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appid", result.getAppId());
		paramMap.put("partnerid", result.getPartner());
		paramMap.put("prepayid", result.getPrepayId());
		paramMap.put("package", result.getPackageStr());
		paramMap.put("noncestr", result.getNonceStr());
		paramMap.put("timestamp", ""+result.getTimeStamp());
		// 设置签名
		result.setSign(WxPayUtil.getSign(paramMap));
		return result;
	}
}
