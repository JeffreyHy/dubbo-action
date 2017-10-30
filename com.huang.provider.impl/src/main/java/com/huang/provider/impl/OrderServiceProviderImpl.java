package com.huang.provider.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huang.provider.OrderServiceProvider;
@Component
public class OrderServiceProviderImpl implements OrderServiceProvider{
	private AtomicLong  counter=new AtomicLong(0L);
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Integer getOrderCount(Integer type) throws Exception{
		/**
		 * 简单实现接口调用计数器
		 */
		counter.getAndIncrement();
		logger.info("-------------------total call "+counter.get()+" times!!!-------");
		if(null==type){
			throw new Exception("type is null!");
		}
		if(type==1){
			return 10;
		}
		else if(type==2){
			return 100;
		}else{
			return 0;
		}
	}
}
