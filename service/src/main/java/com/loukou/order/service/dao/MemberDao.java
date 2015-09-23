package com.loukou.order.service.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.Member;


public interface MemberDao extends CrudRepository<Member, Integer> {

	@Query("SELECT m FROM Member m WHERE cardNo = ?1")
	List<Member> findByCardNo(String cardNo);
	
	@Query("SELECT m FROM Member m WHERE phoneMob = ?1")
	List<Member> findByPhoneMob(String mob);
	
	@Query("SELECT m FROM Member m WHERE phoneMob = ?1 AND phoneChecked = '1'")
	List<Member> findByCheckedPhoneMob(String mob);
	
	@Query("SELECT m FROM Member m WHERE phoneMob = ?1 OR cardNo = ?1")
	List<Member> findMemberByMobCardNo(String mobCardNo);

	@Query("SELECT m FROM Member m WHERE phoneMob = ?1 OR userName = ?2")
	List<Member> findMemberByMobUserName(String mobNo, String userName);

	@Query("SELECT m FROM Member m WHERE phoneMob = ?1 OR cardNo = ?2 OR userName = ?3")
	List<Member> findMemberByMobCardNoUserName(String mobNo, String cardNo,
			String userName);

	@Modifying
	@Query("UPDATE Member SET realName=?2, gender=?3, birthday=?4, siteType=?5, regionId=?6, address=?7 WHERE userId=?1")
	int updateMemberInfo(int userId, String name, int gender, Date birthday,
			String siteType, int regionId, String detail);

	@Modifying
	@Query("UPDATE Member SET cardNo=?2, cardSecret=?3, cardStatus=?4, verifyAmount=?5 WHERE userId=?1")
	int updateCardInfo(int userId, String cardNo, String cardSecret,
			int cardStatus, double verifyAmount);
	
	@Modifying
	@Query("UPDATE Member SET cardNo=?2, cardStatus=1 WHERE userId=?1")
	int rebindCardNo(int userId, String cardNo);

	@Modifying
	@Query("UPDATE Member SET cardSecret=?2 WHERE userId=?1")
	int updateCardSecret(int userId, String cardSecret);

	@Modifying
	@Query("UPDATE Member SET verifyAmount=?2 WHERE userId=?1")
	int updateVerifyAmount(int userId, double verifyAmount);

	@Modifying
	@Query("UPDATE Member SET phoneCheckCode=?2 WHERE userId=?1")
	int updatePhoneCheckCode(int userId, String verificationCode);

	List<Member> findByUserName(String username);

	List<Member> findByUserIdIn(List<Integer> userIds);

}
