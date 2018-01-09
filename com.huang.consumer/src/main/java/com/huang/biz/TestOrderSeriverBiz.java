package com.huang.biz;

import javax.servlet.http.HttpServletRequest;

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
}
