package com.huang.context;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author huangyongbo
 * @date Created by  2018/2/7 16:04
 */
@SpringCloudApplication
@EnableHystrixDashboard
@ComponentScan(basePackages = {
        "com.huang"
})
public class HystrixDashboardApplication extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }
}
