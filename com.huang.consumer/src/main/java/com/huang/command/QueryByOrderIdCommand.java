package com.huang.command;

import com.huang.provider.OrderServiceProvider;
import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by JeffreyHy on 2018/1/18.
 */
public class QueryByOrderIdCommand extends HystrixCommand<Integer> {
    private final static Logger logger = LoggerFactory.getLogger(QueryByOrderIdCommand.class);
    private OrderServiceProvider orderServiceProvider;

    public QueryByOrderIdCommand(OrderServiceProvider orderServiceProvider) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("orderService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("queryByOrderId"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(10)//10秒钟内至少19此请求失败，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(false))//使用dubbo的超时，禁用这里的超时
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties
                        .Setter().withCoreSize(10)));
        this.orderServiceProvider = orderServiceProvider;
    }

    @Override
    protected Integer run() {
        return orderServiceProvider.queryByOrderId();
    }

    /**
     * 失败降级
     *
     * @return
     */
    @Override
    protected Integer getFallback() {
        return -1;
    }
}
