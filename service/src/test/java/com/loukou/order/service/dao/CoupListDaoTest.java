package com.loukou.order.service.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.entity.CoupRule;

public class CoupListDaoTest extends AbstractTestObject {

	@Autowired
	private CoupListDao coupListDao;
	@Autowired 
	private CoupRuleDao coupRuleDao;
	
	@Test
	public void countCouponId() {
		int couponId = 933;
		Integer ret = coupListDao.countCouponId(couponId);
		
		System.out.println(ret);
		
	}

	@Test
	public void getRule() {
		CoupRule coupRule = coupRuleDao.findOne(961);
		System.out.println(coupRule.getMoney());
		}
	}
