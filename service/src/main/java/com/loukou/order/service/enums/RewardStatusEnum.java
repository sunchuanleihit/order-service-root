package com.loukou.order.service.enums;

public enum RewardStatusEnum {
	
 //奖励（5等待中 6 已取消 7 已奖励  8到达上限）
 REWARDSTATUS_WAITING(5,"等待中"),
 //已取消
 REWARDSTATUS_CANCEL(6,"已取消"),
 //已奖励
 REWARDSTATUS_REWARDED(7,"已奖励"),
 //到达上限
 REWARDSTATUS_LIMIT(8,"到达上限");
	
 RewardStatusEnum(int id, String status) {
		this.id = id;
		this.status = status;
	}
	
	private int id;
	
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public static RewardStatusEnum parseType(int id) {
		for (RewardStatusEnum e : RewardStatusEnum.values()) {
			if (e.id == id) {
				return e;
			}
		}
		return REWARDSTATUS_WAITING;
	}
	
	
}

