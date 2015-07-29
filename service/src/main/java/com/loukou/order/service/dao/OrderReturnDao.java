
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
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE OrderReturn set goodsStatus = ?2 where orderIdR in ?1")
	int updateGoodsStatusByOrderIdRList(List<Integer> orderIdRList,int goodsStatus);
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE OrderReturn set goodsStatus = ?2 where orderId = ?1")
	int updateGoodsStatusByOrderId(int orderId,int status);
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE OrderReturn set goodsStatus = ?3 where orderSnMain = ?1 AND sellerId = ?2")
	int updateGoodsStatusByOrderSnMainAndSellerId(String orderSnMain,int sellerId,int status);
	
	List<OrderReturn> findByOrderId(int orderId);
	
	List<OrderReturn> findByOrderSnMainAndSellerId(String orderSnMain,int sellerId);
}

