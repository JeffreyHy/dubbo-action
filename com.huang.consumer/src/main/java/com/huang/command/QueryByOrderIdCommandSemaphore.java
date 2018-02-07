package com.huang.command;

import com.huang.provider.OrderServiceProvider;
import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangyongbo
 * @date Created by  2018/2/6 11:34
 */
public class QueryByOrderIdCommandSemaphore extends HystrixCommand<Integer> {
    private final static Logger logger = LoggerFactory.getLogger(QueryByOrderIdCommandSemaphore.class);
    private OrderServiceProvider orderServiceProvider;

    public QueryByOrderIdCommandSemaphore(OrderServiceProvider orderServiceProvider) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("orderService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("queryByOrderId"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(10)//10秒钟内至少19此请求失败，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)));
        this.orderServiceProvider = orderServiceProvider;
    }

    @Override
    protected Integer run() {
        return orderServiceProvider.queryByOrderId();
    }

    @Override
    protected Integer getFallback() {
        return -1;
    }
}
