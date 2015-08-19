package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderUser;


public interface OrderUserDao extends CrudRepository<OrderUser, Integer> {

	@Query("SELECT count(id) FROM OrderUser WHERE userId=?1")
	int getOrderCountByUserId(int userId);
	
	@Query("SELECT count(id) FROM OrderUser WHERE openId=?1")
	int getOrderCountByOpenId(String openId);
	
	List<OrderUser> findByOrderIdAndUserId(int orderId, int userId);
	
}
