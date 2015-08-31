package com.loukou.order.pay.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alipay.config.AlipayConfig;
import com.loukou.order.pay.dal.BasePayContext;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.resp.dto.ALiPayOrderResultDto;

@Service
public class AliPay {

	private final Logger logger = Logger.getLogger(AliPay.class);

	// 对订单进行支付宝支付准备
	public ALiPayOrderResultDto preparePay(BasePayContext context) {
		double needToPay = context.getAmountToPay();
		// 构造外部支付订单
		String outTradeNo = context.makeOutTrade(context.getUserId(),
				context.getOrderSnMain(), needToPay,
				PaymentEnum.PAY_ALI.getId());
		if (StringUtils.isBlank(outTradeNo)) {
			// 生成外部支付订单失败，滚粗
			// TODO
			logger.error(String
					.format("preparePay alipay fail to make out trade user_id[%d] order_sn_main[%s]",
							context.getUserId(), context.getOrderSnMain()));
			return null;
		} else {
			logger.info(String
					.format("preparePay alipay done to make out trade user_id[%d] order_sn_main[%s] out_trade_no[%s]",
							context.getUserId(), context.getOrderSnMain(),
							outTradeNo));
		}
		// 构造收银台结构
		ALiPayOrderResultDto result = new ALiPayOrderResultDto();
		result.setNeedPay(needToPay);
		//fix url by yy
		result.setNotifyUrl(AlipayConfig.notify_url);
		result.setOrderSnMain(context.getOrderSnMain());
		//外部交易号用orderSnMain
		result.setOutTradeNo(context.getOrderSnMain());
		result.setPartner(AlipayConfig.partner);
		result.setSeller(AlipayConfig.seller_id);
		result.setRsaPrivateKey(AlipayConfig.private_key);
		result.setRsaPublicKey(AlipayConfig.ali_public_key);

		return result;
	}

}
