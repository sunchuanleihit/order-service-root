package com.loukou.order.service.refund;

import com.loukou.order.service.cron.AsyncTaskExecuteResult;
import com.loukou.order.service.entity.OrderPayR;


public class TXKPay  implements IPay{

	@Override
	public AsyncTaskExecuteResult refund(OrderPayR orderPayR) {
		return null;
	}

}
