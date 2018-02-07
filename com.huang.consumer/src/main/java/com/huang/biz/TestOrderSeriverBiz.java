package com.huang.biz;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.huang.command.QueryByOrderIdCommand;
import com.huang.provider.OrderServiceProvider;
import com.netflix.hystrix.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huang.client.OrderServiceClient;
/**
 * 调用示例：http://localhost:9000/order/get_order_count?type=1
 * @author JeffreyHy
 *
 */
@Controller
@RequestMapping("/order")
public class TestOrderSeriverBiz {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private OrderServiceClient  orderServiceClient;
    @Resource
    private OrderServiceProvider orderServiceProvider;
	@RequestMapping("/get_order_count")
    @ResponseBody
    public Integer getOrderCountByCount(Integer type,HttpServletRequest req) {
        try{
        	if(null!=type)
        		return orderServiceClient.getOrderCountByType(type);
            return null;
        }catch (Exception e) {
			logger.error(String.format("orderServiceProvider getOrderCount error. param:%s", type, e));
            return null;
        }
	}

    @RequestMapping("/testQueryByOrderId")
    @ResponseBody
    public String testQueryByOrderId(HttpServletRequest req) {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            HystrixCommand<Integer> command = new QueryByOrderIdCommand(orderServiceProvider);
            Integer r = command.execute();
            String method = r == -1 ? "fallback" : "run";
            logger.info("call {} times,result:{},method:{},isCircuitBreakerOpen:{}", i, r, method, command.isCircuitBreakerOpen());
        }
        //等待6s，使得熔断器进入半打开状态
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (; i < 20; i++) {
            HystrixCommand<Integer> command = new QueryByOrderIdCommand(orderServiceProvider);
            Integer r = command.execute();
            String method = r == -1 ? "fallback" : "run";
            logger.info("call {} times,result:{},method:{},isCircuitBreakerOpen:{}", i, r, method, command.isCircuitBreakerOpen());
        }
        return "success";
    }
}
