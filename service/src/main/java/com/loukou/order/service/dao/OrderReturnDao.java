
package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.OrderReturn;


public interface OrderReturnDao extends PagingAndSortingRepository<OrderReturn, Integer>{

	List<OrderReturn> findByOrderSnMainAndOrderStatus(String orderSnMain, int orderStatus);

	List<OrderReturn> findByBuyerIdAndOrderStatus(int userId, int orderStatus);

	List<OrderReturn> findByOrderSnMain(String orderSnMain);

	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE OrderReturn set goodsStatus = ?2 where orderIdR = ?1")
	int updateGoodsStatusByOrderIdR(int orderIdR,int goodsStatus);
	
	List<OrderReturn> findByOrderId(int orderId);
}

