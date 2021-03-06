package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.CoupRule;


public interface CoupRuleDao extends CrudRepository<CoupRule, Integer>, JpaSpecificationExecutor<CoupRule>{

	List<CoupRule> findByIdIn(List<Integer> couponIds);

	@Transactional
	@Modifying
	@Query("UPDATE CoupRule SET num=?2, residuenum=?3 WHERE id = ?1")
	void update(int couponId, int coupNum, int sumResiduenum);
	
	CoupRule findByCommoncode(String commoncode);

	@Transactional
	@Modifying
	@Query("UPDATE CoupRule SET issue=?2 WHERE id=?1")
	void updateRuleIsuse(Integer ruleId, Integer isuse);
	
}
