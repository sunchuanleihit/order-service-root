package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 邀请码详情表
 *
 */

@Entity
@Table(name="lk_invite_code")
public class InviteCode {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	//userId
	@Column(name = "user_id")
	private int userId = 0;
	
	//邀请码
	@Column(name = "invite_code")
	private String inviteCode;
	
//	//创建时间
//	@Column(name = "create_time")
//	private int createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

//	public int getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(int createTime) {
//		this.createTime = createTime;
//	}
	
	
}

