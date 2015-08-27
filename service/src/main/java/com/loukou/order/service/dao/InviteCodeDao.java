package com.loukou.order.service.dao;


import org.springframework.data.repository.CrudRepository;
import com.loukou.order.service.entity.InviteCode;



public interface InviteCodeDao extends CrudRepository<InviteCode, Integer>{
	//查询邀请码byUserId
	InviteCode findByUserId(int userId);
	
	//查询邀请码byInInviteCode
	InviteCode  findByInviteCode(String inviteCode);
	
	
}
