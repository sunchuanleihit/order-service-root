package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.CoupType;


public interface CoupTypeDao extends CrudRepository<CoupType, Integer>, JpaSpecificationExecutor<CoupType>{

	List<CoupType> findByIdIn(List<Integer> coupTypeIds);
	
	@Query("select c from CoupType c where c.status = 0")
	List<CoupType> queryAll();
	
	@Query("select c from CoupType c where c.status = 0")
	Page<CoupType> queryByPage(Pageable Pageable);

	@Transactional
	@Modifying
	@Query("update CoupType set status = 1 where id = ?1")
	void stopCoupType(Integer id);

	@Transactional
	@Modifying
	@Query("update CoupType set title = ?2, typeid=?3, usenum=?4, newuser=?5, description=?6 where id = ?1")
	void updateType(int id, String title, int typeid, int usenum, int newuser, String description);
}
