package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class BkOrderActionRespDto implements Serializable{
	private static final long serialVersionUID = 6524350857641546192L;
	
	private String actionTime;
	private String note;
	private String actor;
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	
}
