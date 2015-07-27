package com.loukou.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.service.dao.CoupListDao;

@Service
public class CoupListService {

	@Autowired
	private CoupListDao coupListDao;
	
	public boolean useCoupon(int userId, int couponId) {
		int ret = coupListDao.useCoupon(userId, couponId);
		if (ret == 0) {
			// 没有更新，返回错误
			return false;
		}
		return true;
	}
}
