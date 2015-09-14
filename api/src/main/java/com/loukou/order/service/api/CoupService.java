package com.loukou.order.service.api;
import com.loukou.order.service.req.dto.CoupRuleReqDto;
import com.loukou.order.service.resp.dto.CoupRuleRespDto;
import com.loukou.order.service.resp.dto.BkCouponTypeListRespDto;

public interface CoupService {

	CoupRuleRespDto queryCoupRule(CoupRuleReqDto req, Integer pageNum, Integer pageSize);
	
	BkCouponTypeListRespDto typeList(int pageNum, int pageSize);
}
