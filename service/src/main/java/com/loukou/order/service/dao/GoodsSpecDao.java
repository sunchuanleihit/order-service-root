package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.GoodsSpec;


public interface GoodsSpecDao extends PagingAndSortingRepository<GoodsSpec, Integer> {

    @Deprecated
	List<GoodsSpec> findBySpecIdIn(List<Integer> specIds);
	
	@Deprecated
	@Transactional
	@Modifying
	@Query("UPDATE GoodsSpec SET freezstock=freezstock+?2 WHERE specId = ?1")
	int freezeStock(int specId, int stockNum);

	public GoodsSpec findByTaosku(String taosku);

	@Deprecated
	List<GoodsSpec> findByGoodsIdInAndIsDel(Iterable<Integer> goodsIds, int isDel);

	@Deprecated
	List<GoodsSpec> findByGoodsIdInAndIsDelAndIsshow(Iterable<Integer> goodsIds,
			int isDel, int isShow);

	@Deprecated
	List<GoodsSpec> findByGoodsId(int goodsId);
	
	@Deprecated
	List<GoodsSpec> findByBn(String bn);

	@Deprecated
	GoodsSpec findBySpecId(int specId);

	@Deprecated
	@Transactional
	@Modifying
	@Query("UPDATE GoodsSpec SET freezstock=freezstock-?3, taostock=taostock-?2 WHERE specId = ?1")
	int updateBySpecId(int specId, int taostock, int freezestock);

	@Deprecated
	@Transactional
	@Modifying
	@Query("UPDATE GoodsSpec SET freezstock=freezstock-?2 WHERE specId = ?1")
	void updateBySpecId(int specId, int quantity);
	
	@Deprecated
	@Transactional
	@Modifying
	@Query("UPDATE GoodsSpec SET freezstock=freezstock+?2 WHERE specId = ?1")
	void updateAddBySpecId(int specId, int quantity);
}
