package com.loukou.order.service.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.Order;


public interface CoupListDao extends CrudRepository<CoupList, Integer>, JpaSpecificationExecutor<CoupList>{

	List<CoupList> findByUserIdAndIssueAndIschecked(int userId, int issue, int ischecked);

//	@Query("select cl from CoupList cl where userId = ?1 and ischecked=0 and issue=1 and begintime<=NOW() and endtime>=NOW() "
//			+ "AND (usedtime IS NULL OR usedtime='0000-00-00 00:00:00')")
//	List<CoupList> getCoupLists(int userId);

	@Query("SELECT count(id) FROM CoupList WHERE userId = ?1 AND ischecked = 1 AND usedtime >= ?2")
	int getUsedCoupNumber(int userId, Date start);

	
	@Query("SELECT c FROM CoupList c WHERE userId = ?1 AND issue=1 AND ischecked=0 AND begintime<=NOW() AND endtime>=NOW()")
	List<CoupList> getValidCoupLists(int userId);
	
	@Query("SELECT c FROM CoupList c WHERE userId = ?1 AND id=?2 AND issue=1 AND ischecked=0 AND begintime<=NOW() AND endtime>=NOW()")
	CoupList getValidCoupList(int userId, int couponId);

	@Transactional
	@Modifying
	@Query("UPDATE CoupList SET ischecked=1, usedTime=NOW() WHERE userId=?1 AND id=?2 AND ischecked=0 AND issue=1")
	int useCoupon(int userId, int couponId);

	@Transactional
	@Modifying
	@Query("UPDATE CoupList set ischecked = 0, usedtime=NULL where userId = ?2 and commoncode=?1")
	int refundCouponList(String commoncode,int userId);//优惠券状态改成未使用
	
	@Transactional
	@Modifying
	@Query("UPDATE CoupList SET ischecked=1, usedTime=NOW() WHERE userId=?1 AND commoncode=?2 AND ischecked=0 AND issue=1")
	int useCouponByCommoncode(int userId,String commoncode);
	
	CoupList getByUserIdAndCommoncode(int userId,String commoncode);
	
	Page<CoupList> findByUserId(int userId, Pageable pageable);
	
	@Query("SELECT c FROM CoupList c WHERE userId = ?1 AND issue=1 AND ischecked=0 AND endtime<NOW()")
	List<CoupList> getInvalidCoupLists(int userId);
	
	List<CoupList> findByUserIdAndCouponId(int userId, int couponId);

	List<CoupList> findByCouponIdAndOpenid(int couponId, String openId);

	CoupList findByCommoncode(String code);

	@Query(value = "select max(floor(replace(commoncode, ?2, ''))) from tcz_coup_list where coupon_id = ?1", nativeQuery=true)
	Integer findByCouponId(int couponId, String replaceCode);

	List<CoupList> findByUserId(int userId);

	List<CoupList> findByOpenid(String openId);

	@Query("SELECT count(id) FROM CoupList WHERE couponId=?1")
	Integer countCouponId(int couponId);
	
	@Transactional
	@Modifying
	@Query("UPDATE CoupList SET userId=?1, begintime = ?2, endtime=?3, openid=?4, createtime=NOW() WHERE commoncode=?5")
	int update(int userId, Date beginTime, Date endTime, String openId, String commoncode);
	
	List<CoupList> findByCouponId(int couponId);

	@Transactional
	@Modifying
	@Query("UPDATE CoupList set userId=?2, begintime=?3, endtime=?4, createtime=NOW() where id=?1")
	void sendCoup(Integer id, Integer userId, Date begintime, Date endtime);

	@Transactional
	@Modifying
	@Query("UPDATE CoupList set issue = 2 where id = ?1")
	void deleteCoup(Integer id);
	
}
