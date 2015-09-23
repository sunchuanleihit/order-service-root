package com.loukou.order.service.enums;

public enum CoupTypeEnum {
	NONE(-1, ""),
	PRIVATE(0, "私有券"),
	PUBLIC(1, "公有券"),
	DISCOUNT(2, "折扣券");
	
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
	private CoupTypeEnum(int id, String name){
		this.id = id;
		this.name = name;
	}
	public static CoupTypeEnum parseName(int id){
		for(CoupTypeEnum e: CoupTypeEnum.values()){
			if(e.id == id){
				return e;
			}
		}
		return NONE;
	}
	
}
