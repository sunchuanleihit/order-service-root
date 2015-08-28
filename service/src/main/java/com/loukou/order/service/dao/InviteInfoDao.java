package com.loukou.order.service.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.loukou.order.service.entity.InviteList;


public interface InviteInfoDao extends PagingAndSortingRepository<InviteList, Integer>{
	//查询邀请详情列表分页
	Page<InviteList> findByUserId(int userId ,Pageable page);
	
	//查询邀请列表不分页
	List<InviteList> findByUserId(int userId);
	//查询奖励金额
	@Query("SELECT  coalesce( sum(l.reward),0) FROM InviteList  l  where l.userId=?1")
	double sumRewardByUserId(int userId);
	
	//根据手机号查找邀请信息
	List<InviteList> findByPhoneMob(String phoneMob);
	
	//根据手机号和发放查找邀请信息
	List<InviteList> findByPhoneMobAndIfGetcoupon(String phoneMob,int ifGetcoupon);
	
	//更新邀请列表
	@Modifying
	@Query("update  InviteList  set ifGetcoupon=1 where  phoneMob=?1 ")
	int  updateIfGetcouponByPhone(String phoneMob);
	
}
