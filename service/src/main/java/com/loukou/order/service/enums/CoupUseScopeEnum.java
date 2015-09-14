package com.loukou.order.service.enums;

public enum CoupUseScopeEnum {
	NONE(-1,""),
	ALL(0,"全场券"),
	SHOP(1,"店铺券"),
	BAND(2,"品牌券"),
	KIND(3,"分类券");
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
	private CoupUseScopeEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static CoupUseScopeEnum parseName(int id){
		for(CoupUseScopeEnum e: CoupUseScopeEnum.values()){
			if(e.getId() == id){
				return e;
			}
		}
		return NONE;
	}

}
