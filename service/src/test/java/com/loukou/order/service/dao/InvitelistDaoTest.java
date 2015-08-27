package com.loukou.order.service.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.entity.InviteCode;
import com.loukou.order.service.entity.InviteList;

public class InvitelistDaoTest extends AbstractTestObject {
	@Autowired
	private InviteInfoDao inviteDao;
	@Autowired
	private InviteCodeDao invite;
	
	@Test
	public void sumReward() {
		int userId=23;
		System.out.println(inviteDao.sumRewardByUserId(userId));
	}
	@Test
	public  void queryInvite(){
		int userId=21;
		System.out.println(invite.findByUserId(userId).getInviteCode());
	}
	
	@Test
	public void saveInvite() {
		InviteCode a=new InviteCode();
		a.setUserId(24);
		a.setInviteCode("0vzstk18");
		System.out.println(invite.save(a));
	}
	@Test
	public  void queryInviteList(){
		int userId=10;
		PageRequest p=new PageRequest(0,10, new Sort(
                Sort.Direction.ASC, "createdTime"));
		Page<InviteList> pList= inviteDao.findByUserId(userId, p);
		System.out.println(pList.getContent().size());
	}
}
