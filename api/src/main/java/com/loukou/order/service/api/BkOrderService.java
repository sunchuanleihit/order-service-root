package com.loukou.order.service.api;

import java.util.List;

import com.loukou.order.service.req.dto.CssOrderReqDto;
import com.loukou.order.service.resp.dto.BkOrderListRespDto;
import com.loukou.order.service.resp.dto.CssOrderRespDto;

/**
 * 客服系统用到的订单服务
 * @author sunchuanlei
 *
 */
public interface BkOrderService {
	public List<CssOrderRespDto> queryOrderList(int page, int rows, CssOrderReqDto cssOrderReqDto);
	
	public BkOrderListRespDto orderDetail(String orderSnMain);
}
