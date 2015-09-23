package com.loukou.order.service.enums;

public enum CoupNewUserEnum {
	NONE(-1, ""),
	UNLIMITED(0, "不限"),
	NEW(1, "新会员"),
	OLD(2, "老会员");
	
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private CoupNewUserEnum(int id, String name){
		this.id = id;
		this.name = name;
	}
	public static CoupNewUserEnum parseName(int id){
		for(CoupNewUserEnum e: CoupNewUserEnum.values()){
			if(e.id == id){
				return e;
			}
		}
		return NONE;
	}
	
}
