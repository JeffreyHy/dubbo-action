package com.huang.client;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.huang.provider.OrderServiceProvider;

@Service
public class OrderServiceClient {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private OrderServiceProvider orderServiceProvider;
	
	public Integer getOrderCountByType(Integer type) {
		try {
			return orderServiceProvider.getOrderCount(type);
		} catch (Exception e) {
			logger.warn(String.format("orderServiceProvider getOrderCount error. param:%s", type, e));
			return null;
		}
	}
}
