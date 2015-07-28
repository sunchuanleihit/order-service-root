package com.loukou.order.service.cron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderPayRDao;
import com.loukou.order.service.entity.AsyncTask;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.entity.OrderPayR;
import com.loukou.order.service.refund.IPay;
import com.loukou.order.service.refund.PayFactory;

/**
 * 退款异步任务
 * @author linhua
 *
 */
@Service
public class RefundAsyncTaskExecuter extends AbsAsyncTaskExecuter{

	@Autowired
	OrderDao orderDao;
	
	@Autowired
	OrderPayDao orderPayDao;
	
	@Autowired
	OrderPayRDao orderPayRDao;
	
	@Autowired
	PayFactory payFactory;
	
	@Override
	public AsyncTaskExecuteResult execute(AsyncTask task) {
		OrderPayR orderPayR = orderPayRDao.findOne(task.getActionKey());

		IPay pay = payFactory.getPay(orderPayR.getPaymentId());
		if(pay!=null){
			return pay.refund(orderPayR);
		}

		return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_FAILED.getId(), "TaskExecuter不存在");
	}
}
