
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
	@Query("UPDATE WeiCangGoodsStore set stockS = stockS - ?3, freezstock=freezstock - ?4 where specId = ?1 and storeId=?2")
	int updateBySpecIdAndStoreId(int specId, int storeId, int stockS, int freezstock);

	@Transactional
	@Modifying
	@Query("UPDATE WeiCangGoodsStore set freezstock=freezstock - ?3 where specId = ?1 and storeId=?2")
	int updateBySpecIdAndStoreId(int specId, int sellerId, int freezstock);
	
	@Transactional
	@Modifying
	@Query("UPDATE WeiCangGoodsStore set freezstock=freezstock + ?3 where specId = ?1 and storeId=?2")
	int updateAddBySpecIdAndStoreId(int specId, int sellerId, int freezstock);

	@Transactional
    @Modifying
    @Query("UPDATE WeiCangGoodsStore set stockS = stockS - ?4, freezstock=freezstock - ?5 ,updateTime = ?3  where specId = ?1 and storeId=?2")
    void updateBySpecIdAndStoreIdAndUpdateTime(int specId, int storeId,Date updateTime ,int stockS, int freezestock);

	@Transactional
	@Modifying
	@Query("UPDATE WeiCangGoodsStore set freezstock=freezstock - ?3, updateTime=?4 where specId = ?1 and storeId=?2")
	void updateBySpecIdAndStoreIdAndUpdateTime(int specId, int sellerId, int freezestock, Date updateTime);

}

