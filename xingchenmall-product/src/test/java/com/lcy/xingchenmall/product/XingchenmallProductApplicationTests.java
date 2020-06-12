package com.lcy.xingchenmall.product;

import com.lcy.xingchenmall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@SpringBootTest
class XingchenmallProductApplicationTests {

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void redisson(){
        System.out.println(redissonClient);
    }

    @Test
    public void testStringRedisTemplate(){
        //hello wrold
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //保存
        ops.set("hello","world"+ UUID.randomUUID().toString());
        //查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据是："+hello);

    }

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径{}", Arrays.asList(catelogPath));
    }

    @Test
    void contextLoads() {
    }

//    @Test
//    public void testUpload() throws FileNotFoundException {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-beijing.aliyuncs.com";
//        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = "<yourAccessKeyId>";
//        String accessKeySecret = "<yourAccessKeySecret>";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 上传文件流。
//        InputStream inputStream = new FileInputStream("<yourlocalFile>");
//        ossClient.putObject("<yourBucketName>", "<yourObjectName>", inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//    }

}
