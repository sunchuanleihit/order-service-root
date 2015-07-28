package com.loukou.order.service.cron;

public class AsyncTaskExecuteResult {
	private int status;
	private String message;
	
	
	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public AsyncTaskExecuteResult(int status, String message) {
		this.status = status;
		this.message = message;
	}


	public enum AsyncTaskExecuteResultStatusEnum{
		STATUS_SUCCESS(1),
		
		STATUS_FAILED(2);
		
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		private AsyncTaskExecuteResultStatusEnum(int id) {
			this.id = id;
		}
		
	}
}
