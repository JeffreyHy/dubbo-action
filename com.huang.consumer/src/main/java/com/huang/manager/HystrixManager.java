package com.huang.manager;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.huang.hystrix.DubboHystrixCommand;
import com.netflix.hystrix.HystrixCommand;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * HystrixManager 随容器启动，然后解析所有Command类上的注解（自定义）
 * 维护Provider和Command Class对象的关系。Map<String,Class<HystrixCommand>>
 * 通过反射创建Command对象
 *
 * @author huangyongbo
 * @date Created by  2018/4/11 15:05
 */
@Component
public class HystrixManager implements InitializingBean {
    private static Map<String, Class<? extends HystrixCommand<Result>>> providerCommand = new HashMap<>();

    public HystrixCommand<Result> getCommand(Invoker<?> invoker, Invocation invocation) {
        Class<? extends HystrixCommand<Result>> commandClass =
                providerCommand.get(invoker.getInterface().getSimpleName());
        if (commandClass != null) {
            try {
                Constructor<? extends HystrixCommand<Result>>
                        constructor = commandClass.getConstructor(Invoker.class, Invocation.class);
                if (constructor != null) {
                    return constructor.newInstance(invoker, invocation);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //扫描Command类上的注解
        providerCommand.putIfAbsent("orderServiceProvider", DubboHystrixCommand.class);
    }
}
