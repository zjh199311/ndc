package com.zhongjian;




import javax.annotation.Resource;

import com.zhongjian.dao.jdbctemplate.AddressDao;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.service.order.OrderService;
import com.zhongjian.task.AddressTask;
import com.zhongjian.util.TaskUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {
    @Resource
    OrderService a;
    
    @Resource
    OrderDao orderDao;
    @Resource
    AddressDao addressDao;
    
    @Autowired
    AddressTask addressTask;
    
    

    @Test
    public void test() {
    	TaskUtil.start(1);
    	addressTask.setAddressTask(6, 16);
}
}