package com.huang.hystrix;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;
import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * dubbo请求熔断器
 * Created by JeffreyHy on 2018/1/9.
 */
public class DubboHystrixCommand extends HystrixCommand<Result> {
    private final static Logger logger = LoggerFactory.getLogger(DubboHystrixCommand.class);
    private static final int DEFAULT_THREAD_POOL_CORE_SIZE = 30;
    private Invoker invoker;
    private Invocation invocation;

    public DubboHystrixCommand(Invoker invoker, Invocation invocation) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))//按接口分组
                .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
                        invocation.getArguments() == null ? 0 : invocation.getArguments().length)))//每个方法对应一个command
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(10)//10秒钟内至少19此请求失败，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(false))//使用dubbo的超时，禁用这里的超时
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties
                        .Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()))));//线程池为30
        this.invoker = invoker;
        this.invocation = invocation;
    }

    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("ThreadPoolCoreSize", DEFAULT_THREAD_POOL_CORE_SIZE);
            if (logger.isDebugEnabled()) {
                logger.debug("ThreadPoolCoreSize:{}", size);
            }
            return size;
        }
        return DEFAULT_THREAD_POOL_CORE_SIZE;
    }

    @Override
    protected Result run() {
        if (logger.isDebugEnabled()) {
            logger.debug("invoke method:{}",invocation.getMethodName());
        }
        return invoker.invoke(invocation);
    }

    /**
     * 失败降级
     *
     * @return
     */
    @Override
    protected Result getFallback() {
        if(logger.isDebugEnabled()){
            logger.debug("call getFallback");
        }
        return new RpcResult();
    }
}
