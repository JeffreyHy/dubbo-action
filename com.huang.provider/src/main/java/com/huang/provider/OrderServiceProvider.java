package com.huang.provider;

/**
 * 订单服务
 *
 * @author Administrator
 */
public interface OrderServiceProvider {
    /**
     * 按类型查询订单数量
     *
     * @param type
     * @returne
     */
    Integer getOrderCount(Integer type) throws Exception;

    /**
     * 按订单id查询
     *
     * @return
     */
    Integer queryByOrderId();

    /**
     * 重置相关计数器
     *
     * @return
     */
    void reset();
}