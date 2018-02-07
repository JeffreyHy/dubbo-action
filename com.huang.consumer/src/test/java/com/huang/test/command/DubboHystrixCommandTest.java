package com.huang.test.command;

import com.huang.provider.OrderServiceProvider;
import com.huang.test.AbstractTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author huangyongbo
 * @date Created by  2018/2/2 13:39
 */
public class DubboHystrixCommandTest extends AbstractTestCase {
    private final static Logger logger = LoggerFactory.getLogger(DubboHystrixCommandTest.class);
    @Resource
    private OrderServiceProvider orderServiceProvider;
    /**
     * 使用dubboFilterc处理服务熔断与降级,需要启用HystrixFilter
     */
    @Test
    public void testQueryByOrderId() {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            Integer r = orderServiceProvider.queryByOrderId();
            logger.info("call {} times,result:{}", i, r);
        }
        try {
            //等待6s，使得熔断器进入半打开状态
            Thread.sleep(6000);
        } catch (InterruptedException e) {
        }
        for (; i < 20; i++) {
            Integer r = orderServiceProvider.queryByOrderId();
            logger.info("call {} times,result:{}", i, r);
        }
    }
}
