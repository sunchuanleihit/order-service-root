package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.CoupList;


public interface CoupListDao extends CrudRepository<CoupList, Integer>{

//<<<<<<< HEAD
	List<CoupList> findByUserIdAndIssueAndIschecked(int userId, int issue, int ischecked);

	@Query("select cl from CoupList cl where userId = ?1 and ischecked=0 and issue=1 and begintime<=NOW() and endtime>=NOW() "
			+ "AND (usedtime IS NULL OR usedtime='0000-00-00 00:00:00')")
	List<CoupList> getCoupLists(int userId);

	
//=======
//	@Query("SELECT c FROM CoupList WHERE userId = ?1 AND issure=1 AND ischecked=0 AND begintime<=NOW() AND endtime>=NOW()")
//	List<CoupList> getValidCoups(int userId);
//>>>>>>> for merge
}
