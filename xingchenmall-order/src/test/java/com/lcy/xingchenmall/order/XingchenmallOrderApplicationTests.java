package com.lcy.xingchenmall.order;

import com.lcy.xingchenmall.order.entity.OrderEntity;
import com.lcy.xingchenmall.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XingchenmallOrderApplicationTests {

    @Autowired
    OrderService orderService;

    @Test
    void contextLoads() {
        OrderEntity byId = orderService.getById("1");
        System.out.println(byId.toString());
    }


}
