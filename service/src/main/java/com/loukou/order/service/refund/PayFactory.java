package com.loukou.order.service.refund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.PaymentEnum;

@Service
public class PayFactory {
	
	@Autowired
	AliPay aliPay;
	
	@Autowired
	CouponPay couponPay;
	
	@Autowired
	TXKPay txkPay;
	
	@Autowired
	VAccountPay vAccountPay;
	
	@Autowired
	WeiXinPay weiXinPay;
	
	public IPay getPay(int paymentId){
		if(paymentId == PaymentEnum.PAY_ALI.getId()){
			 return aliPay;
		}
		
		if(paymentId == PaymentEnum.PAY_YHQ.getId()){
			 return couponPay;
		}
		
		if(paymentId == PaymentEnum.PAY_APP_WEIXIN.getId()){
			 return weiXinPay;
		}
		
		if(paymentId == PaymentEnum.PAY_TXK.getId()){
			 return txkPay;
		}
		
		if(paymentId == PaymentEnum.PAY_VACOUNT.getId()){
			 return vAccountPay;
		}
		
		return null;
	}
}
