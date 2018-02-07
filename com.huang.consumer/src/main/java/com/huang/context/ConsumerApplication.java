package com.huang.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ComponentScan(basePackages = {"com.huang"})
@SpringBootApplication
@ImportResource(locations = {"classpath*:spring/context-*"})
public class ConsumerApplication extends WebMvcConfigurerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(ConsumerApplication.class, args);
        logger.info("--------consumer start success!!!-----------------");
    }
}