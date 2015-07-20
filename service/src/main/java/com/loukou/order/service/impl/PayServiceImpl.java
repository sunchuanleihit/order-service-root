package com.loukou.order.service.impl;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.loukou.order.service.api.PayService;
import com.loukou.order.service.constants.OrderReqParams;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.PaymentDao;
import com.loukou.order.service.dao.TczcountRechargeDao;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.TczcountRecharge;
import com.loukou.order.service.req.dto.XmlParamsDto;
import com.loukou.order.service.resp.dto.AbstractPayOrderRespDto;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.txk.req.TxkCardRowRespVO;
import com.loukou.pos.client.txk.req.TxkMemberCardsRespVO;

public class PayServiceImpl implements PayService {
	
	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired 
	private MemberDao memberDao;
	
	@Autowired 
	private TczcountRechargeDao tczcountRechargeDao;
	
	@Autowired 
	private PaymentDao paymentDao;
	
	@Autowired 
	private OrderActionDao orderActionDao;
	
	
	@Override
	public AbstractPayOrderRespDto payOrder(int userId, int payType,
			int paymentId, String orderSnMain, int isTaoxinka, int isVcount) {
		AbstractPayOrderRespDto resp = new AbstractPayOrderRespDto();
		//支付类别 4支付宝 207微信支付
		if(userId <= 0 || paymentId <= 0 || StringUtils.isBlank(orderSnMain)) {
			resp.setCode(400);
			return resp;
		}
		if(payType <= 0) {
			payType = 2;//支付类别 1货到付款2在线支付
		}
		if(isTaoxinka <= 0) {
			isTaoxinka = 0;//是否使用淘心卡 1是 0否
		}
		if(isVcount <= 0) {
			isVcount = 0;//是否使用虚拟账户 1是 0否
		}
		
		if(StringUtils.startsWith(orderSnMain, "CZR")) {
			submitBillPaymentVcount(userId, payType, paymentId, orderSnMain, isTaoxinka, isVcount);
		} else {
			submitBillPayment(userId, payType, paymentId, orderSnMain, isTaoxinka, isVcount);
		}
	       
		return resp;
	}

	//提交支付方式(虚拟充值专用)
	/*
	 @ $userId  用户ID
	 @ $payType  支付类别 1货到付款2在线支付
	 @ $paymentId  支付类别 4支付宝 207微信支付
	 @ $orderSnMain  主订单号
	 @ $isTaoxinka  是否使用淘心卡支付
	 @ $isVcount  是否使用虚拟账户
	 @ $couponId  优惠券ID
	*/
	private void submitBillPaymentVcount(int userId, int payType,
			int paymentId, String orderSnMain, int isTaoxinka, int isVcount){
		Member member = memberDao.findOne(userId);

		//选择支付方式action
		OrderAction orderAction = new OrderAction();
		orderAction.setAction(20);
		orderAction.setOrderSnMain(orderSnMain);
		orderAction.setActor(member.getUserName());
		orderAction.setActionTime(new Date());
		orderAction.setNotes("选择支付方式" + payType);
		orderActionDao.save(orderAction);
		
		TczcountRecharge tczcountRecharge = tczcountRechargeDao.findByOrderSnMain(orderSnMain);
		//订单类别：material=普通商品,booking=预售商品,self_sales=第三方商家
		double needToPay = tczcountRecharge.getMoney();//还需要支付的金额
		
		if(needToPay > 0) {
			//paymentId 4支付宝 207微信支付
			if(paymentId == 207) {
				
			} else if(paymentId == 4) {
				
			}
			
		}
		
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

	//提交支付方式
	/*
	 @ $userId  用户ID
	 @ $payType  支付类别 1货到付款2在线支付
	 @ $paymentId  支付类别 4支付宝 207微信支付
	 @ $orderSnMain  主订单号
	 @ $isTaoxinka  是否使用淘心卡支付
	 @ $isVcount  是否使用虚拟账户
	 @ $couponId  优惠券ID
	*/
	private void submitBillPayment(int userId, int payType,
			int paymentId, String orderSnMain, int isTaoxinka, int isVcount){
		
		
	}
	
	
	//---------------------------
	/**
	 * 支付方式支持：支付宝、微信、虚拟账户、桃心卡
	 */
	private void taoxinkaPay(int userId, double money) {
		double leftMoney = getUserTxk(userId);
		if(leftMoney == 0) {
//			return false;
		}
		if(DoubleUtils.round(money, 2) > DoubleUtils.round(leftMoney, 2)) {
			
		}

    
	}
	
	
	/* 获取当前登录用户的淘心卡余额 */
	private double getUserTxk(int userId){
		double money = 0;
		if(userId <= 0){
	        return money;
	    }
	    TxkMemberCardsRespVO resp = AccountTxkProcessor.getProcessor().getTxkListByUserId(userId);
	    if(resp.getTotal() != 0) {
	    	for(TxkCardRowRespVO row : resp.getRows()) {
	    		money = DoubleUtils.add(money, row.getResidueAmount());
	    	}
	    }
	    return money;
	}
	

}
