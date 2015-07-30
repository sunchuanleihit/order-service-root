
package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.loukou.order.service.entity.OrderReturn;


public interface OrderReturnDao extends PagingAndSortingRepository<OrderReturn, Integer>{

	List<OrderReturn> findByOrderSnMainAndOrderStatus(String orderSnMain, int orderStatus);

	List<OrderReturn> findByBuyerIdAndOrderStatus(int userId, int orderStatus, Pageable pageable);

	
}

