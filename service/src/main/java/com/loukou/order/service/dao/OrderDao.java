
package com.loukou.order.service.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.Order;


public interface OrderDao extends PagingAndSortingRepository<Order, Integer>{

	Order findByTaoOrderSn(String taoOrderSn);
	
	List<Order> findByOrderSnMain(String orderSnMain);

	@Query("SELECT o FROM Order o WHERE shippingId=?1 AND status=15 AND finishedTime>=?2 AND finishedTime<=?3")
	List<Order> getCvsFininshedOrders(int cvsId, int start, int end);
	
	@Query("SELECT o FROM Order o WHERE status=15 AND sellerId IN (?1) AND finishedTime >= ?2 AND finishedTime < ?3")
	List<Order> getByStoreIdsAndFinishedTime(List<Integer> storeIds, int start, int end);


	Page<Order> findBySellerIdAndPayStatusAndStatusIn(int storeId, int payStatus, List<Integer> statusList, Pageable Pageable);

	List<Order> findBySellerIdAndPayTimeGreaterThan(int storeId,
			int startPayTime);

	Page<Order> findBySellerIdAndPayStatus(int storeId, int payStatus, Pageable Pageable);
	
	List<Order> findByOrderSnMainAndSellerId(String orderSnMain, int sellerId);
	
	@Query("SELECT o FROM Order o WHERE orderSnMain=?1 ")
	List<Order> getCvsBackOrders(String orderSnMain);
	
	@Transactional
	@Modifying
	@Query("UPDATE Order set shippingNo = ?1 where orderId = ?2")
	int updateShippingNo(String shippingNo,int orderId);

	Page<Order> findByOrderSnMainIn(Iterable<String> orderSnMains, Pageable pageable);

	List<Order> findByshippingNoIn(List<String> shippingNo);

	Page<Order> findByBuyerIdAndIsDel(int userId, int isDel, Pageable pageable);

<<<<<<< HEAD
	@Transactional
=======
>>>>>>> add testCase
	@Modifying
	@Query("UPDATE Order set status = ?2 where orderId = ?1")
	void updateOrderStatus(int orderId, int status);

	Order findByOrderId(int orderId);
	
<<<<<<< HEAD
	@Transactional
=======
>>>>>>> add testCase
	@Modifying
	@Query("UPDATE Order set payStatus = ?3, orderPayed = ?2 where orderId = ?1")
	Order updateOrderPayedAndStatus(int orderId, double payedMoney, int status);

	Page<Order> findByBuyerIdAndIsDelAndPayStatusAndStatusIn(int userId, int isDel, int payStatus,
			List<Integer> statusList, Pageable pagealbe);

	List<Order> findByOrderSnMainAndPayStatus(String orderSnMain, int payStatus);

	Page<Order> findBySellerIdAndTypeAndStatus(int sellerId,String type,int status,Pageable page);
	
	@Modifying
    @Query("UPDATE Order set status = ?2, receiveNo=?3 where orderId = ?1")
    void updateOrderStatusAndreceiveNo(int orderId, int status, String receiveNo);
	
	@Modifying
    @Query("UPDATE Order set status = ?1, finishedTime=?2 where orderId = ?3")
	void updateStatusAndFinishedTime(int status,int finishedTime,int orderId);
	
}

