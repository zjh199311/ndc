package com.zhongjian;




import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhongjian.dao.jdbctemplate.AddressDao;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dao.jdbctemplate.UserDao;
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
    @Autowired
    UserDao userDao;
    

    @Test
    public void test() {
    	List<Map<String, Object>> reList = orderDao.getOrderDetailByRoid(421);
    	for (Map<String, Object> map : reList) {
			System.out.println((String) map.get("order_sn"));
			System.out.println((Integer) map.get("is_appointment"));
		}
    	
}
}