package com.loukou.order.service.api;

import java.util.List;

import com.loukou.order.service.req.dto.CssOrderReqDto;
import com.loukou.order.service.resp.dto.BkOrderListRespDto;
import com.loukou.order.service.resp.dto.CssOrderRespDto;
import com.loukou.order.service.resp.dto.GoodsListDto;

/**
 * 客服系统用到的订单服务
 * @author sunchuanlei
 *
 */
public interface BkOrderService {
	public List<CssOrderRespDto> queryOrderList(int page, int rows, CssOrderReqDto cssOrderReqDto);
	
	public BkOrderListRespDto orderDetail(String orderSnMain);
	
	public BkOrderListRespDto queryBkOrderList(String sort,String order,int pageNum, int pageSize, CssOrderReqDto cssOrderReqDto);

	public List<GoodsListDto> getOrderGoodsList(int orderId);
}
