package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.CouponSn;



public interface CouponSnDao extends PagingAndSortingRepository<CouponSn, Integer>{

	List<CouponSn> findBycouponIdAndUserId(int couponId, int userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE CouponSn set ischecked = 0, usedtime='' where userId = ?2 and commoncode=?1")
	int refundCouponSn(String commoncode,int userId);//优惠券状态改成未使用

	
}
