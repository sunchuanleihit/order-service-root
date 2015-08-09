package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class LkStatusItemDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2047288492950651694L;
	private int id;
	private String value;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public LkStatusItemDto(int id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
	public LkStatusItemDto() {
		super();
	}
}
