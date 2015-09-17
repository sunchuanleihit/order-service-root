package com.loukou.order.service.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderGoods;


public interface OrderGoodsDao extends CrudRepository<OrderGoods, Integer>{

	List<OrderGoods> findByOrderId(int orderId);

	List<OrderGoods> findByOrderIdIn(Collection<Integer> orderIds);

	@Modifying
	@Query(value="DELETE FROM tcz_order_goods WHERE order_id = ?1", 
		nativeQuery=true)
	int deleteByOrderId(int orderId);

	@Query("SELECT coalesce(sum(quantity), 0) FROM OrderGoods WHERE orderId IN (?1) AND specId = ?2")
	int sumGoods(List<Integer> orderIds, int specId);

	@Query("SELECT coalesce(sum(quantity), 0) FROM OrderGoods WHERE storeId=?1 AND specId=?2 AND timestamp > ?3")
	int sumGoods(int storeId, int specId, Date date);
}
