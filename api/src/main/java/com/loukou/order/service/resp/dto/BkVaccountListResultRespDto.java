package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkVaccountListResultRespDto extends ResponseCodeDto implements Serializable{
	private static final long serialVersionUID = -856341187713314346L;

	public BkVaccountListResultRespDto(int code, String message) {
		super(code, message);
	}
	
	private List<BkVaccountRespDto> bkVaccountRespDtoList = new ArrayList<BkVaccountRespDto>();
	private Integer totalElement = 0;

	public List<BkVaccountRespDto> getBkVaccountRespDtoList() {
		return bkVaccountRespDtoList;
	}
	public void setBkVaccountRespDtoList(List<BkVaccountRespDto> bkVaccountRespDtoList) {
		this.bkVaccountRespDtoList = bkVaccountRespDtoList;
	}
	public Integer getTotalElement() {
		return totalElement;
	}
	public void setTotalElement(Integer totalElement) {
		this.totalElement = totalElement;
	}
	
	

}
