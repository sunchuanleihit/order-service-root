package com.loukou.pay.lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.loukou.order.service.constants.OrderReqParams;
import com.loukou.order.service.dao.OrderPaySignDao;
import com.loukou.order.service.entity.OrderPaySign;
import com.loukou.order.service.enums.PaySignStatusEnum;
import com.loukou.pay.service.common.PayReqContent;

public class ALiPay {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private OrderPaySignDao orderPaySignDao;
	
	// 对订单进行支付宝支付
	public boolean pay(PayReqContent content) {
		boolean needToPay = false;
		
		return needToPay;
	}

	 /* 获取通知地址 */
    private String createNotifyUrl(String orderSnMain, String type){
        if(StringUtils.isEmpty(orderSnMain) || StringUtils.isEmpty(type)){
            logger.info("miss order_sn_main or payment_type");
           return null;
        }
        StringBuilder url = new StringBuilder("index.php?app=shop.paynotify&act=");
        url.append(type).append("&main=").append(orderSnMain);
        return url.toString();
    }
	
    /* 获取返回地址 */
    private String createReturnUrl(String orderSnMain, String type){
    	if(StringUtils.isEmpty(orderSnMain) || StringUtils.isEmpty(type)){
            logger.info("miss order_sn_main or payment_type");
           return null;
        }
    	StringBuilder url = new StringBuilder("index.php?app=shop.payreturn&act=");
        url.append(type).append("&main=").append(orderSnMain);
        return url.toString();
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
    
    /* 获取外部交易号 */
    private String getTradeSn(String orderSnMain, double money, int paymentId) {
    	if(paymentId <= 0) {
    		logger.error(String.format("危险！%s的支付方式为空", orderSnMain));
    		return null;
    	}

    	String outTradeSn = makeTradeSn();
    	
//    	OrderPaySign  orderPaySign = orderPaySignDao.findByOutOrderSn(outTradeSn);
    	
    	if(orderPaySignDao.findByOutOrderSn(outTradeSn) != null) {//重复了
    		return getTradeSn(orderSnMain, money, paymentId);
    	}
    	
    	OrderPaySign orderPaySign = new OrderPaySign();
    	orderPaySign.setOrderSnMain(orderSnMain);
    	orderPaySign.setOutOrderSn(outTradeSn);
    	orderPaySign.setPayId(paymentId);
    	orderPaySign.setMoney(money);
    	orderPaySign.setcTime(new Date().getTime() / 1000);
    	orderPaySign.setStatus(PaySignStatusEnum.STATUS_READY.getStatus());
//    	OrderPaySign paySign = orderPaySignDao.save(entity);
    	if(orderPaySignDao.save(orderPaySign) != null) {
    		return outTradeSn;
    	}
    	return outTradeSn;
    }
    /* 生成外部交易号 */
    private String makeTradeSn() {
    	Random random = new Random();
    	int randomInt = random.nextInt(999999) + 1;
    	StringBuilder outTradeSn = new StringBuilder(new SimpleDateFormat("yyyyMM").format(new Date()))
    				.append(StringUtils.leftPad(String.valueOf(randomInt), 6, "0"));
    	
        return outTradeSn.toString();
    }
    
}
