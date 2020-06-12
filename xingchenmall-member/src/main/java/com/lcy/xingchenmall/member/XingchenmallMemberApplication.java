package com.lcy.xingchenmall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.lcy.xingchenmall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class XingchenmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(XingchenmallMemberApplication.class, args);
    }

}
