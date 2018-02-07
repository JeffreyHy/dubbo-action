package com.huang.command;

import com.huang.provider.OrderServiceProvider;
import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * HystrixObservableCommand 使用,
 *
 * @author huangyongbo
 * @date Created by  2018/2/2 13:58
 */
public class QueryByOrderIdObservableCommand extends HystrixObservableCommand<Integer> {
    private final static Logger logger = LoggerFactory.getLogger(QueryByOrderIdObservableCommand.class);
    private OrderServiceProvider orderServiceProvider;

    public QueryByOrderIdObservableCommand(OrderServiceProvider orderServiceProvider) {
        super(HystrixObservableCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("orderService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("queryByOrderId"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(10)//10秒钟内至少19此请求失败，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(false))//使用dubbo的超时，禁用这里的超时
        );
        this.orderServiceProvider = orderServiceProvider;
    }

    @Override
    protected Observable<Integer> construct() {
        return Observable.just(orderServiceProvider.queryByOrderId());
    }

    @Override
    protected Observable<Integer> resumeWithFallback() {
        if (logger.isDebugEnabled()) {
            logger.debug("call getFallback");
        }
        return Observable.just(-1);
    }
}
