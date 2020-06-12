package com.lcy.xingchenmall.search;

import com.alibaba.fastjson.JSON;
import com.lcy.xingchenmall.search.config.XingchenmallElasticConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XingchenmallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 测试存储数据到es
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("user");
        indexRequest.id("1"); //数据的id
//        indexRequest.source("userName","zhangsan","age",18,"gender","男");
        User user = new User();
        String str = JSON.toJSONString(user);
        indexRequest.source(str, XContentType.JSON); //要保存到内容

        //执行操作
        IndexResponse index = client.index(indexRequest, XingchenmallElasticConfig.COMMON_OPTIONS);

        //提取有用的响应数据
        System.out.println(index);
    }

    class User{
        private String userName;
        private String gender;
        private Integer age;
    }

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

}
