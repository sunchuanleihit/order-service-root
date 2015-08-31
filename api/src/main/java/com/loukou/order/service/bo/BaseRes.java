package com.loukou.order.service.bo;

import java.io.Serializable;

//响应实体
public class BaseRes<T> implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1018913434223737023L;
	private String code;
    private String message;
    private T result;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
    
}
