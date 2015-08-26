package com.loukou.order.service.api;

import java.util.List;

import com.loukou.order.service.req.dto.CssOrderReqDto;
import com.loukou.order.service.resp.dto.BkOrderListRespDto;
import com.loukou.order.service.resp.dto.BkOrderReturnListRespDto;
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
	/**
	 * 查找所有订单
	 * @param sort
	 * @param order
	 * @param pageNum
	 * @param pageSize
	 * @param cssOrderReqDto
	 * @return
	 */
	public BkOrderListRespDto queryBkOrderList(String sort,String order,int pageNum, int pageSize, CssOrderReqDto cssOrderReqDto);
	
	/**
	 * 查找未生成退款单的订单
	 * @param sort
	 * @param order
	 * @param pageNum
	 * @param pageSize
	 * @param cssOrderReqDto
	 * @return
	 */
	public BkOrderListRespDto queryBkOrderNoReturnList(String sort, String order, int pageNum, int pageSize, CssOrderReqDto cssOrderReqDto);
	
	/**
	 * 查找待退款订单
	 * @param sort
	 * @param order
	 * @param pageNum
	 * @param pageSize
	 * @param cssOrderReqDto
	 * @return
	 */
	public BkOrderReturnListRespDto queryBkOrderToReturn(String sort, String order, int pageNum, int pageSize, CssOrderReqDto cssOrderReqDto);
	
	/**
	 * 取消反向订单
	 * @param orderIdR
	 * @return
	 */
	public String cancelOrderReturn(Integer orderIdR);

	public List<GoodsListDto> getOrderGoodsList(int orderId);

}
