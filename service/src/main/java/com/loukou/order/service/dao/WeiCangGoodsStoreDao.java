
package com.loukou.order.service.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.WeiCangGoodsStore;


public interface WeiCangGoodsStoreDao extends PagingAndSortingRepository<WeiCangGoodsStore, Integer>{

	WeiCangGoodsStore findBySpecIdAndStoreId(int specId, int sellerId);

	@Transactional(value = "transactionManagerMall")
	@Modifying
	@Query("UPDATE WeiCangGoodsStore set stockS = stockS - ?3, freezestock=freezestock - ?4 where specId = ?1 and storeId=?2")
	void updateBySpecIdAndStoreId(int specId, int storeId, int stockS, int freezestock);

	@Transactional(value = "transactionManagerMall")
	@Modifying
	@Query("UPDATE WeiCangGoodsStore set freezestock=freezestock - ?3 where specId = ?1 and storeId=?2")
	void updateBySpecIdAndStoreId(int specId, int sellerId, int freezestock);


}

