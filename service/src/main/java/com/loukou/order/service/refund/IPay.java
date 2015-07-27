package com.loukou.order.service.refund;

import com.loukou.order.service.cron.AsyncTaskExecuteResult;
import com.loukou.order.service.entity.OrderPayR;

public interface IPay {
	public AsyncTaskExecuteResult refund(OrderPayR orderPayR);
}
