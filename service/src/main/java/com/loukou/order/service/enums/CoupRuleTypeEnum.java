package com.loukou.order.service.enums;

public enum CoupRuleTypeEnum {
	NONE(-1, ""),
	ENOUGH(0, "满减券"),
	CASH(1, "现金券"),
	DISCOUNT(2, "折扣券"),
	OFFLINE(3,"线下券"),
	SELFGET(4,"自提点专用");
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
	private CoupRuleTypeEnum(int id, String name){
		this.id = id;
		this.name = name;
	}
	public static CoupRuleTypeEnum parseName(int id){
		for(CoupRuleTypeEnum e: CoupRuleTypeEnum.values()){
			if(e.id == id){
				return e;
			}
		}
		return NONE;
	}
	
}
