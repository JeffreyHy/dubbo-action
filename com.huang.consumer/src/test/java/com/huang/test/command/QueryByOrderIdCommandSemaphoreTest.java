package com.huang.test.command;

import com.huang.command.QueryByOrderIdCommandSemaphore;
import com.huang.provider.OrderServiceProvider;
import com.huang.test.AbstractTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author huangyongbo
 * @date Created by  2018/2/6 11:52
 */
public class QueryByOrderIdCommandSemaphoreTest extends AbstractTestCase {
    private final static Logger logger = LoggerFactory.getLogger(QueryByOrderIdCommandSemaphoreTest.class);
    @Resource
    private OrderServiceProvider orderServiceProvider;

    @Test
    public void testExecuteCommand() throws InterruptedException {
        ExecutorService executor= Executors.newCachedThreadPool();
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            final String count = String.valueOf(i);
            executor.submit(() -> {
                Integer r = new QueryByOrderIdCommandSemaphore(orderServiceProvider).execute();
                logger.info("call {} times,result:{}", count, r);
            });
        }

        //等待12s，使得熔断器进入半打开状态
        Thread.sleep(12000);

        for (; i < 20; i++) {
            final String count = String.valueOf(i);
            executor.submit(() -> {
                Integer r = new QueryByOrderIdCommandSemaphore(orderServiceProvider).execute();
                logger.info("call {} times,result:{}", count, r);
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
