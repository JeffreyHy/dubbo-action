package com.huang.provider.impl;

import com.huang.provider.OrderServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderServiceProviderImpl implements OrderServiceProvider {
    private final static Logger logger = LoggerFactory.getLogger(OrderServiceProviderImpl.class);

    private AtomicLong counter = new AtomicLong(0L);
    private AtomicInteger OrderIdCounter = new AtomicInteger(0);

    @Override
    public Integer getOrderCount(Integer type) throws Exception {
        /**
         * 简单实现接口调用计数器
         */
        counter.getAndIncrement();
        logger.info("-------------------total call " + counter.get() + " times!!!-------");
        if (null == type) {
            throw new Exception("type is null!");
        }
        if (type == 1) {
            return 10;
        } else if (type == 2) {
            return 100;
        } else {
            return 0;
        }
    }

    @Override
    public Integer queryByOrderId() {
        int c=OrderIdCounter.getAndIncrement();
        if (logger.isDebugEnabled()){
            logger.debug("OrderIdCounter:{}",c);
        }
        if (c < 10) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
            }
        }
        return c;
    }

    @Override
    public void reset() {
        OrderIdCounter.getAndSet(0);
        counter.getAndSet(0);
    }
}
