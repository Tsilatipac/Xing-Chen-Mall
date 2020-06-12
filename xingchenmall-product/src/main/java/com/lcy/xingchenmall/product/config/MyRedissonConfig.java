package com.lcy.xingchenmall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyRedissonConfig {
    // 所有对Redisson对使用都是通过RedissonClient对象
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException{
        // 创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.20.10.4:6379");
        // 根据Config创建出Redisson实例
        return Redisson.create(config);
    }
}
