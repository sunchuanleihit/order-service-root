package com.loukou.order.service.resp.dto;

import java.io.Serializable;
//为了区别与其他API返回的Response,Order命名为Ｏ
public class OResponseDto<T> implements Serializable{
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
