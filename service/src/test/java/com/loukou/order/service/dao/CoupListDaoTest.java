package com.loukou.order.service.dao;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupRule;

public class CoupListDaoTest extends AbstractTestObject {

	@Autowired
	private CoupListDao coupListDao;
	@Autowired 
	private CoupRuleDao coupRuleDao;
	@Autowired 
	private  OrderDao orderDao;
	
	
	@Test
	public void countCouponId() {
		int couponId = 933;
		Integer ret = coupListDao.countCouponId(couponId);
		
		System.out.println(ret);
		
	}

	@Test
	public void getRule() {
//		CoupRule coupRule = coupRuleDao.findOne(961);
//		System.out.println(coupRule.getMoney());
	//System.out.println("++++++++"+orderDao.IfExistOrder(1032752));
		CoupList coupL = new CoupList();
		
		coupL.setOpenid("12112");
		coupL.setCommoncode("11111");
		coupL.setCouponId(1135);
		CoupList coupL1 = new CoupList();
		coupL1= new CoupList();
		BeanUtils.copyProperties(coupL, coupL1);
//		coupL1.setOpenid("12122");
//		coupL1.setCouponId(1135);
		coupL1.setCommoncode("111241");
		
		coupListDao.save(Lists.newArrayList(coupL,coupL));
	
		coupListDao.save(coupL1);
		}
	}
