package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import com.loukou.order.service.api.PayService;
import com.loukou.order.service.constants.OrderReqParams;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.PaymentDao;
import com.loukou.order.service.dao.TczcountRechargeDao;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.entity.TczcountRecharge;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
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
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderPayDao orderPayDao;
	
	/**
	"userId": 1//用户ID
    "payType":1//支付类别 1货到付款2在线支付
    "paymentId": 1//支付类别 4支付宝 207微信支付
    "orderSnMain": 1//订单号
    "isTaoxinka": 1//是否使用淘心卡 1是 0否
    "isVcount": 1//是否使用虚拟账户 1是 0否
	 */
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
		boolean needToPay = true;
		
		if(StringUtils.startsWith(orderSnMain, "CZR")) {//充值
			submitBillPaymentVcount(userId, payType, paymentId, orderSnMain, isTaoxinka, isVcount);
		} else {
			//检查桃心卡和虚拟账户的余额
			if(isVcount == 1) {
				VAcountPay vAcountPay = new VAcountPay();
				needToPay = vAcountPay.payVaccount(userId, orderSnMain, fillOrderModels(orderSnMain));
			}
			
			if(isTaoxinka == 1 && needToPay == true) {
				TXKPay txkPay = new TXKPay();
				needToPay = txkPay.payTXK(userId, orderSnMain, fillOrderModels(orderSnMain));
			}
			
			if(payType == PaymentEnum.PAY_ALI.getId() && needToPay == true) {
				ALiPay aLiPay = new ALiPay();
				
				aLiPay.pay(orderSnMain, fillOrderModels(orderSnMain));
			}
			if(payType == PaymentEnum.PAY_APP_WEIXIN.getId() && needToPay == true) {
				WeiXinPay weiXinPay = new WeiXinPay();
				weiXinPay.pay(orderSnMain, fillOrderModels(orderSnMain));
			}
//			submitBillPayment(userId, payType, paymentId, orderSnMain, isTaoxinka, isVcount);
		}
		
		
		
	       
		return resp;
	}

	
	private List<OrderModels> fillOrderModels(String orderSnMain) {
		List<OrderModels> orderModelsList = new ArrayList<OrderModels>();
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		if(CollectionUtils.isEmpty(orders)) {
			return null;
		}
		for(Order order : orders) {
			OrderModels orderModels = new OrderModels();
			orderModels.setOrder(order);
			List<OrderPay> orderPayList = orderPayDao.findByOrderId(order.getOrderId());
			orderModels.setPays(orderPayList);
			orderModelsList.add(orderModels);
		}
		
		return orderModelsList;
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
		
		//订单总金额
		TczcountRecharge tczcountRecharge = tczcountRechargeDao.findByOrderSnMain(orderSnMain);
		//订单类别：material=普通商品,booking=预售商品,self_sales=第三方商家
		double needToPay = tczcountRecharge.getMoney();//还需要支付的金额
		XmlParamsDto xmlParamsDto = new XmlParamsDto();
		xmlParamsDto.setAppid(OrderReqParams.APPID);
		xmlParamsDto.setMchId(OrderReqParams.MCHID);
//		xmlParamsDto.setNonceStr(createNoncestr(32));//32位
		xmlParamsDto.setNotifyUrl(OrderReqParams.NOTIFY_URL);
		xmlParamsDto.setBody("订单号:" + orderSnMain);//商品描述
		xmlParamsDto.setOutTradeNo(orderSnMain);
//		xmlParamsDto.setTotalFee(totalFee);
		if(needToPay > 0) {
			//paymentId 4支付宝 207微信支付
			if(paymentId == PaymentEnum.PAY_APP_WEIXIN.getId()) {
				WeiXinPay weiXinPay = new WeiXinPay();
//				weiXinPay.pay(orderSnMain, xmlParamsDto, allModels, response);
			} else if(paymentId == PaymentEnum.PAY_ALI.getId()) {
				
			}
			
		}
		
	}


}
