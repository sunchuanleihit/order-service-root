package com.loukou.order.service.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.OrderRemark;

public interface OrderRemarkDao extends PagingAndSortingRepository<OrderRemark, Integer>, JpaSpecificationExecutor<OrderRemark>{
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE OrderRemark set closed = 1 , userClosed = ?1, closedTime = NOW() where id = ?2")
	int updateOrderCloseByIdList(String userClosed, Integer id);
	
	@Query("SELECT o from OrderRemark o where o.type = 1 and o.orderSnMain = ?1 order by o.id ASC")
	List<OrderRemark> getHandoverByOrderSnMain(String orderSnMain);
}
