package com.huang.hystrix;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;

/**
 * dubbo filter,拦截dubbo调用，为每个方法配置熔断器
 * Created by JeffreyHy on 2018/1/9.
 */
//@Activate(group = Constants.CONSUMER)
public class HystrixFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        DubboHystrixCommand command = new DubboHystrixCommand(invoker, invocation);
        return command.execute();
    }
}
