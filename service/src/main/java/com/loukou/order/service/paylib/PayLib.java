package com.loukou.order.service.paylib;

import com.loukou.order.service.resp.dto.PaymentResultDto;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.txk.req.TxkCardRowRespVO;
import com.loukou.pos.client.txk.req.TxkMemberCardsRespVO;

public class PayLib {

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
		double alsoToPay = tczcountRecharge.getMoney();//还需要支付的金额
		
		if(alsoToPay > 0) {
			//paymentId 4支付宝 207微信支付
//			PaymentResultDto paymentResult = payIt(orderSnMain, paymentId, money, jumpUrl);
			if(payIt() == true) {
				
			}
			
		}
		
		//订单总金额
//		$strSQL="SELECT money as total FROM `tcz_tczcount_recharge` WHERE order_sn_main='".$orderSnMain."'";
//		$arrOrderInfo=$G_SHOP->DBCA->getRow($strSQL);
//		$order_amount = $arrOrderInfo['total'];
//
//		//订单类别：material=普通商品,booking=预售商品,self_sales=第三方商家
//		$orderType = "material";
//
//		//已付金额
//		$payed_amount=0;

		//还需支付的金额
		$alsoToPay=$order_amount-$payed_amount;//还需要支付的金额

		/************以下是还需支付的 $alsoToPay************/
		if($alsoToPay > 0){
			//paymentId 4支付宝 207微信支付
			$objPay = new Taoczpay($userId);
			$pay_info=array('payment_id'=>$paymentId,'money'=>$alsoToPay,'action'=>$orderType);
			$pay_result=$objPay->pay_it($orderSnMain,$pay_info);
			if($pay_result==true){
				$jump_url=$objPay->location;
				$asign_mobile = $objPay->asign_mobile;
				return array('error'=>0,'msg'=>'','url_return' =>$asign_mobile,'money'=>$objPay->money,'jump_url'=>$jump_url);
			}else{
				 $result = $objPay->get_error();
				 return array('error' => '1','msg'=>$result[0]['msg']);
			}
		}else{
			return array('error' => '0','msg'=>'订单支付成功');
		}
		return array('error' => '1','msg'=>'异常!支付失败');
	}

	private boolean payIt(String orderSnMain, int paymentId, double money, String jumpUrl) {
		List<Payment> payments = (List<Payment>) paymentDao.findAll();
		for(Payment payment : payments) {
			
			
			if(method_exists($this, "_" . $info['payment_code'] . "_pay")){
            	call_user_func_array(array($this, "_" . $info['payment_code'] . "_pay"), array($info));
        	}else{
        		$this->_def_pay($info);
        	}
		}
	}
	
	//---------------------------
	/**
	 * 支付方式支持：支付宝、微信、虚拟账户、桃心卡
	 */
	private PaymentResultDto taoxinkaPay(int userId, double money) {
		double leftMoney = getUserTxk(userId);
		PaymentResultDto paymentResultDto = new PaymentResultDto();
		if(leftMoney == 0) {
			paymentResultDto.setError("支付失败,淘心卡余额不足!");
			return paymentResultDto;
		}
		if(DoubleUtils.round(money, 2) > DoubleUtils.round(leftMoney, 2)) {
			
		}
		

//        $leftMoney = getUserTxk($this->user_id);
//        if($leftMoney === false){
//            $this->_error('please_login');
//            $this->_is_pay = false;
//            return false;
//        }
//
//        if(round($this->money,2) > round($leftMoney,2)){
//            $this->_error('支付失败,淘心卡余额不足!');
//            $this->_is_pay = false;
//            return false;
//        }
//        $db=&db();
//        $user_name = $db->getOne("SELECT user_name FROM tcz_member WHERE user_id='{$this->user_id}'"); //用户名称
//        $url = TXK_PAYURL;
//
//        $params = array(
//            'app_key' => txk_appKey,
//            'call_id' => gmtime(),
//            'method' => 'cardpay',
//            'paymoney' => $this->money,
//            'uid' => $this->user_id,
//            'uname' => $user_name,
//            'orderid' => $this->order_sn_main,
//            'ordercode' => $this->order_sn_main
//        );
//        
//        $retID = $this->madePaySign();
//        if(!$retID){
//            $this->_error('make order_pay_sign error');
//            $this->_is_pay = false;
//            return false;
//        }
//        $request = _taoRequest($params);
//        $result = http($url, 'POST', $request);
//        
//        $ret = tcz_json_decode($result, true);
//        if($ret['msg'] == 'success'){
//            $this->overPaySign($retID);
//            $this->_is_pay = true;
//        }else{
//            $this->_error($ret['msg']);
//            $this->_is_pay = false;
//        }
		return paymentResultDto;
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
