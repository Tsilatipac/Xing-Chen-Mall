package com.lcy.xingchenmall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class XingchenmallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(XingchenmallCouponApplication.class, args);
    }

}
