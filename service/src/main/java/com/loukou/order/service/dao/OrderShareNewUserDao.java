package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderShareNewUser;


public interface OrderShareNewUserDao extends CrudRepository<OrderShareNewUser, Integer>{
	List<OrderShareNewUser> findByPhoneMobAndStatus(String phone, int status);

	@Transactional
	@Modifying
	@Query("UPDATE OrderShareNewUser set status = ?2 where id = ?1")
	int updateStatusById(int id,int status);
}
