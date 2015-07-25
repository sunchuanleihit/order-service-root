package com.loukou.order.service.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.CoupList;


public interface CoupListDao extends CrudRepository<CoupList, Integer>{

	List<CoupList> findByUserIdAndIssueAndIschecked(int userId, int issue, int ischecked);

//	@Query("select cl from CoupList cl where userId = ?1 and ischecked=0 and issue=1 and begintime<=NOW() and endtime>=NOW() "
//			+ "AND (usedtime IS NULL OR usedtime='0000-00-00 00:00:00')")
//	List<CoupList> getCoupLists(int userId);

	@Query("SELECT count(id) FROM CoupList WHERE userId = ?1 AND usedtime >= ?2")
	int getUsedCoupNumber(int userId, Date start);

	
	@Query("SELECT c FROM CoupList c WHERE userId = ?1 AND issue=1 AND ischecked=0 AND begintime<=NOW() AND endtime>=NOW()")
	List<CoupList> getValidCoupLists(int userId);
	
	@Query("SELECT c FROM CoupList c WHERE userId = ?1 AND couponId=?2 AND issue=1 AND ischecked=0 AND begintime<=NOW() AND endtime>=NOW()")
	CoupList getValidCoupList(int userId, int couponId);

	@Transactional
	@Modifying
	@Query("UPDATE CoupList SET ischecked=1, usedTime=NOW() WHERE userId=?1 AND couponId=?2 AND ischecked=0 AND issue=1")
	int useCoupon(int userId, int couponId);

}
