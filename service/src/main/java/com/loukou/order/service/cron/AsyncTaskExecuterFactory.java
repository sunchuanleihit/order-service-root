package com.loukou.order.service.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.service.entity.AsyncTask;
import com.loukou.order.service.enums.AsyncTaskActionEnum;

@Service
public class AsyncTaskExecuterFactory {
	
	@Autowired
	RefundAsyncTaskExecuter refundTaskExecuter;
	
	public IAsyncTaskExecuter getExecuter(AsyncTask task) {

		if (task.getAction() == AsyncTaskActionEnum.ACTION_REFUND.getId()) {
			return refundTaskExecuter;
		}

		return null;
	}
}
