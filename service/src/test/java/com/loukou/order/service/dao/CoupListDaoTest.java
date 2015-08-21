package com.loukou.order.service.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;

public class CoupListDaoTest extends AbstractTestObject {

	@Autowired
	private CoupListDao coupListDao;
	
	@Test
	public void countCouponId() {
		int couponId = 933;
		Integer ret = coupListDao.countCouponId(couponId);
		
		System.out.println(ret);
		
	}
}
