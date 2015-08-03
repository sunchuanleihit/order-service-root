
package com.loukou.order.service.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.WeiCangGoodsStore;


public interface WeiCangGoodsStoreDao extends PagingAndSortingRepository<WeiCangGoodsStore, Integer>{

	WeiCangGoodsStore findBySpecIdAndStoreId(int specId, int sellerId);

	@Transactional
	@Modifying
	@Query("UPDATE WeiCangGoodsStore set stockS = stockS - ?3, freezstock=freezstock - ?4 , updateTime=?5 where specId = ?1 and storeId=?2")
	void updateBySpecIdAndStoreIdAndUpdateTime(int specId, int storeId, int stockS, int freezestock, Date updateTime);

	@Transactional
	@Modifying
	@Query("UPDATE WeiCangGoodsStore set freezstock=freezstock - ?3, updateTime=?4 where specId = ?1 and storeId=?2")
	void updateBySpecIdAndStoreIdAndUpdateTime(int specId, int sellerId, int freezestock, Date updateTime);

}

