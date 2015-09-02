package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.Goods;

public interface GoodsDao extends CrudRepository<Goods, Integer> {

	List<Goods> findByGoodsIdIn(List<Integer> goodsIds);

	List<Goods> findByStoreIdAndNewCateId(int storeId, int newCateId);
	
	List<Goods> findByStoreIdAndNewCateIdOne(int storeId, int newCateIdOne);
	
	List<Goods> findByStoreIdAndNewCateIdTwo(int storeId, int newCateIdTwo);
	
	List<Goods> findByStoreIdAndNewCateIdThree(int storeId, int newCateIdThree);

	List<Goods> findByStoreId(int storeId);
	
	@Query(value="SELECT DISTINCT(new_cate_id_1) FROM tcz_goods WHERE store_id = ?1 AND is_del=0 AND new_cate_id_1>0", nativeQuery=true)
	List<Integer> getNewCateIdOneByStoreId(int storeId);
	
	@Transactional
	@Modifying
	@Query("UPDATE Goods SET isDel=1, ifShow=0 WHERE goodsId=?1")
	int deleteGoods(int goodsId);
	
	@Query("SELECT c FROM Goods c WHERE goodsId in (?1) AND isDel=0")
	List<Goods> findByGoodsIds(List<Integer> goodsIds);

	@Query("SELECT c FROM Goods c WHERE newCateId in (?1) AND isDel=0")
	List<Goods> findByNewCateIdsIn(List<Integer> newCateIds);

	@Query("SELECT g FROM Goods g WHERE goodsId IN (?1) AND if_show=1 AND closed=0")
	List<Goods> getValid(List<Integer> goodsIds);
	
	@Query("SELECT DISTINCT(g.defaultImage) FROM Goods g WHERE goodsId in (?1)")
	List<String> getImageList(List<Integer> goodsIds);
	
	@Query("SELECT c FROM Goods c WHERE goodsId in (?1)")
	List<Goods> getGoodsByIDs(List<Integer> goodsIds);

	Goods findByGoodsId(int goodsId);
}
