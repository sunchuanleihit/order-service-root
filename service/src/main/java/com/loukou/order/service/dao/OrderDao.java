
package com.loukou.order.service.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.Order;


public interface OrderDao extends PagingAndSortingRepository<Order, Integer>, JpaSpecificationExecutor<Order>{
	List<Order> findByTaoOrderSn(String taoOrderSn);
	
	List<Order> findByOrderSn(String orderSn);
	
	List<Order> findByOrderSnMain(String orderSnMain);

	@Query("SELECT o FROM Order o WHERE shippingId=?1 AND status=15 AND finishedTime>=?2 AND finishedTime<=?3")
	List<Order> getCvsFininshedOrders(int cvsId, int start, int end);

	@Query("SELECT o FROM Order o WHERE orderSnMain=?1 AND status=15")
	List<Order> getFininshedOrders(String orderSnMain);
	
	@Query("SELECT o FROM Order o WHERE status=15 AND sellerId IN (?1) AND finishedTime >= ?2 AND finishedTime < ?3")
	List<Order> getByStoreIdsAndFinishedTime(List<Integer> storeIds, int start, int end);
	
	@Query("SELECT count(o) FROM Order o WHERE status > 2 AND sellerId = ?1 AND addTime >= ?2 AND addTime < ?3")
	int countValidOrderBetweenAddTime(int storeId, int start, int end);

	List<Order> findByStatusAndSellerIdAndAddTimeBetween(int orderStatus,int storeId, int start, int end);

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
	
	List<Order> findByOrderSnMainIn(List<String> orderSnMains);
	
	List<Order> findByUseCouponNoIn(List<String> couponNos);
	
	List<Order> findByshippingNoIn(List<String> shippingNo);

	Page<Order> findByBuyerIdAndIsDel(int userId, int isDel, Pageable pageable);
	
	Page<Order> findByBuyerIdAndIsDelAndPayStatusInAndSource(int userId, int isDel, List<Integer> payStatusList,int source,Pageable pageable);
	
	Page<Order> findByBuyerId(int userId, Pageable pageable);

	@Transactional
	@Modifying
	@Query("UPDATE Order set status = ?2 where orderId = ?1")
	int updateOrderStatus(int orderId, int status);

	Order findByOrderId(int orderId);
	
	@Transactional
	@Modifying
	@Query("UPDATE Order set payStatus = ?3, orderPayed = ?2 where orderId = ?1")
	Order updateOrderPayedAndStatus(int orderId, double payedMoney, int status);

	Page<Order> findBySellerId(int sellerId,Pageable page);

	@Transactional(value = "transactionManagerMall")
	@Modifying
	@Query("UPDATE Order set goodsReturnStatus = ?2 where orderId = ?1")
	int updateGoodsReturnStatus(int orderId, int status);

	@Query("SELECT COUNT(*) FROM Order d WHERE d.orderSnMain = ?1 and d.status <> ?2")
	int countOrderByOrderSnMainAndStatusNot(String orderSnMain,int status);

	Page<Order> findByBuyerIdAndIsDelAndPayStatusAndStatusIn(int userId, int isDel, int payStatus,
			List<Integer> statusList, Pageable pagealbe);
	Page<Order> findByBuyerIdAndIsDelAndPayStatusAndStatusInAndSourceNot(int userId, int isDel, int payStatus,
			List<Integer> statusList,int source, Pageable pagealbe);
	
	List<Order> findByOrderSnMainAndPayStatus(String orderSnMain, int payStatus);

	Page<Order> findBySellerIdAndStatusAndTypeIn(int sellerId,int status,List<String> types,Pageable page);
	
	Page<Order> findBySellerIdAndStatusAndFinishedTimeBetweenAndTypeIn(int sellerId,int status,int startTime,int endTime,List<String> types,Pageable page);
	
	Page<Order> findBysellerIdAndStatusAndPayStatusInAndTypeIn(int storeId, int orderStatus, List<Integer> payed,
            List<String> types, Pageable pagenation);
	@Modifying
    @Query("UPDATE Order set status = ?2, receiveNo=?3 where orderId = ?1")
	@Transactional
    int updateOrderStatusAndreceiveNo(int orderId, int status, String receiveNo);
	
	@Modifying
    @Query("UPDATE Order set status = ?1, finishedTime=?2 where orderId = ?3")
	@Transactional
	int updateStatusAndFinishedTime(int status,int finishedTime,int orderId);

	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE Order set status = ?3 where orderSnMain = ?1 and status != ?2")
	int updateOrderStatusByOrderSnMainAndNotStatus(String orderSnMain, int oldStatus, int newStatus);
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE Order set status = ?3 where orderId = ?1 and status != ?2")
	int updateOrderStatusByOrderIdAndNotStatus(int orderId, int oldStatus, int newStatus);

	List<Order> findByBuyerIdAndStatusNotIn(int userId, List<Integer> statusList);
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE Order set status = ?2 where orderSnMain = ?1")
	int updateStatusByOrderSnMain(String orderSnMain, int status);

	@Query("SELECT 1 FROM Order WHERE buyerId=?1")
	String IfExistOrder(int buyerId);
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE Order set needShiptime = ?2,needShiptimeSlot=?3 where orderSnMain = ?1")
	int updateNeedShipTimeByOrderSnMain(String orderSnMain,Date needShiptime, String needShiptimeSlot);
	
	@Transactional
	@Modifying
	@Query("UPDATE Order set payId = ?1, payStatus = 1, payTime = addTime, orderPayed = goodsAmount+shippingFee where orderSnMain = ?2")
	int updateOrderPayId(int payId,String orderSnMain);
}

