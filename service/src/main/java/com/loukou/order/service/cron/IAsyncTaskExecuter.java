package com.loukou.order.service.cron;

import com.loukou.order.service.entity.AsyncTask;

public interface IAsyncTaskExecuter {
	public AsyncTaskExecuteResult execute(AsyncTask task);
}
