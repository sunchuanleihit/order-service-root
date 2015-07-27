package com.loukou.order.service.cron;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.service.dao.AsyncTaskDao;
import com.loukou.order.service.entity.AsyncTask;
import com.loukou.order.service.enums.AsyncTaskStatusEnum;

@Service("asyncTaskTask")
public class AsyncTaskCron {
	private static final int MAXTHREADCOUNT=10;
	@Autowired
	AsyncTaskDao asyncTaskDao;
	
	@Autowired
	AsyncTaskExecuterFactory taskExecuterFactory;
	
	public void run(){
		List<AsyncTask> taskList = asyncTaskDao.findByStatus(AsyncTaskStatusEnum.STATUS_NEW.getId());
		//ExecutorService pool = Executors.newFixedThreadPool(MAXTHREADCOUNT);

		for (AsyncTask asyncTask : taskList) {
//			AsyncTaskThread taskThread = new AsyncTaskThread(asyncTask);
//			pool.execute(taskThread);
			IAsyncTaskExecuter executer = taskExecuterFactory.getExecuter(asyncTask);
			if(executer!=null){
				AsyncTaskExecuteResult result = executer.execute(asyncTask);
				
				asyncTask.setStatus(result.getStatus()==AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_SUCCESS.getId()?AsyncTaskStatusEnum.STATUS_SUCCESS.getId():AsyncTaskStatusEnum.STATUS_FAILED.getId());
				asyncTask.setExecuteMessage(result.getMessage());
				
				asyncTaskDao.save(asyncTask);
			}
		}
		
		//pool.shutdown();
	}

	
	class AsyncTaskThread extends Thread{
		AsyncTask task;
		
		public AsyncTaskThread(AsyncTask task){
			this.task=task;
		}
		
		@Override
		public void run() {
			IAsyncTaskExecuter executer = taskExecuterFactory.getExecuter(task);
			if(executer!=null){
				executer.execute(task);
			}
		}
	}
}
