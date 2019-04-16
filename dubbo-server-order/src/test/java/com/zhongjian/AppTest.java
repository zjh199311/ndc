package com.zhongjian;


import javax.annotation.Resource;

import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;
import com.zhongjian.service.order.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {
    @Resource
    OrderService orderService;
    
    @Resource
    OrderDao orderDao;
    

    @Test
    public void test() {
       
    }
}
