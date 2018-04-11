package com.huang.hystrix;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.huang.manager.HystrixManager;
import com.netflix.hystrix.HystrixCommand;

import javax.annotation.Resource;

/**
 * dubbo filter,拦截dubbo调用，为每个方法配置熔断器
 * Created by JeffreyHy on 2018/1/9.
 */
//@Activate(group = Constants.CONSUMER)
public class HystrixFilter implements Filter {
    @Resource
    private HystrixManager hystrixManager;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //通过接口名称获取配置开关，控制是否开启熔断
        /*if (!hystrixIsOpen) {
            return invoker.invoke(invocation);
        }*/
        //调用HystrixManager提供的工厂方法创建HystrixCommand对象
        HystrixCommand<Result> command = hystrixManager.getCommand(invoker, invocation);
        if (command == null) {
            return invoker.invoke(invocation);
        }
        return command.execute();
    }
}
