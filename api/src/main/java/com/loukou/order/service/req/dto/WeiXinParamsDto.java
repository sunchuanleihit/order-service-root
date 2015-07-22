package com.loukou.order.service.req.dto;

import java.sql.Timestamp;

public class WeiXinParamsDto {
	private String appid;
	private String partnerid;
	private String prepayid;
	private String packageStr;
	private String noncestr;
	private long timestamp;
	private String sign;
	private String weiXinResult;
	private double needToPay;
	
	
	public double getNeedToPay() {
		return needToPay;
	}

	public void setNeedToPay(double needToPay) {
		this.needToPay = needToPay;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getWeiXinResult() {
		return weiXinResult;
	}

	public void setWeiXinResult(String weiXinResult) {
		this.weiXinResult = weiXinResult;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getPackageStr() {
		return packageStr;
	}

	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
