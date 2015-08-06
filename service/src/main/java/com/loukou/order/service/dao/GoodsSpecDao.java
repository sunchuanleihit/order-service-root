package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.GoodsSpec;


public interface GoodsSpecDao extends PagingAndSortingRepository<GoodsSpec, Integer> {

	List<GoodsSpec> findBySpecIdIn(List<Integer> specIds);
	
	@Transactional
	@Modifying
	@Query("UPDATE GoodsSpec SET freezstock=freezstock+?2 WHERE specId = ?1")
	int freezeStock(int specId, int stockNum);

	public GoodsSpec findByTaosku(String taosku);

	List<GoodsSpec> findByGoodsIdInAndIsDel(Iterable<Integer> goodsIds, int isDel);

	List<GoodsSpec> findByGoodsIdInAndIsDelAndIsshow(Iterable<Integer> goodsIds,
			int isDel, int isShow);

	List<GoodsSpec> findByGoodsId(int goodsId);
	
	List<GoodsSpec> findByBn(String bn);

	GoodsSpec findBySpecId(int specId);

	@Transactional
	@Modifying
	@Query("UPDATE GoodsSpec SET freezstock=freezstock+?3, taostock=taostock-?2 WHERE specId = ?1")
	int updateBySpecId(int specId, int taostock, int freezestock);

	@Transactional
	@Modifying
	@Query("UPDATE GoodsSpec SET freezstock=freezstock-?2 WHERE specId = ?1")
	void updateBySpecId(int specId, int quantity);
}
