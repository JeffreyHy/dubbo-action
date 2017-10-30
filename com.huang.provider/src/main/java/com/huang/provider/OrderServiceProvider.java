package com.huang.provider;
/**
 * 订单服务
 * @author Administrator
 *
 */
public interface  OrderServiceProvider {
	/**
	 * 按类型查询订单数量
	 * @param type
	 * @returne
	 */
	public Integer getOrderCount(Integer type) throws Exception;
}