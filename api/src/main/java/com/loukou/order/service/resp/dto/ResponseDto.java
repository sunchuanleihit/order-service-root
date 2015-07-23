package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ResponseDto<T> implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -2146701666385595500L;
    private T result;
    private int code;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseDto [result=" + result + ", code=" + code + "]";
    }
    
}
