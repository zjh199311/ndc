package com.zhongjian;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhongjian.dao.jdbctemplate.AddressDao;
import com.zhongjian.dao.jdbctemplate.OrderDao;
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
    @Resource
    AddressDao addressDao;
    

    @Test
    public void test() {
    	System.out.println(addressDao.getAddressById(1));
    }
}
