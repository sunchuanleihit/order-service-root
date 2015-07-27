package com.loukou.order.service.refund;

import org.springframework.stereotype.Service;

import com.loukou.order.service.cron.AsyncTaskExecuteResult;
import com.loukou.order.service.entity.OrderPayR;

@Service
public class AliPay implements IPay{

	@Override
	public AsyncTaskExecuteResult refund(OrderPayR orderPayR) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
