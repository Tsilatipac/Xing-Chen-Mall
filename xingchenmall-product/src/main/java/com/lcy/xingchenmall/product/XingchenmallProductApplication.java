package com.lcy.xingchenmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableFeignClients(basePackages = "com.lcy.xingchenmall.product.feign")
@EnableDiscoveryClient
@MapperScan("com.lcy.xingchenmall.product.dao")
@SpringBootApplication
public class XingchenmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(XingchenmallProductApplication.class, args);
    }

}
