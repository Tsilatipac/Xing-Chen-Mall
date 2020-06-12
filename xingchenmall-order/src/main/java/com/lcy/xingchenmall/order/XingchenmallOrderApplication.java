package com.lcy.xingchenmall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.lcy.xingchenmall.order.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class XingchenmallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(XingchenmallOrderApplication.class, args);
    }

}
