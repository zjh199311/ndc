package com.zhongjian;




import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhongjian.dao.jdbctemplate.AddressDao;
import com.zhongjian.dao.jdbctemplate.MarketDao;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dao.jdbctemplate.UserDao;
import com.zhongjian.service.order.OrderService;
import com.zhongjian.task.AddressTask;

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
    MarketDao marketDao;
    
    @Autowired
    AddressTask addressTask;
    @Autowired
    UserDao userDao;
    

    @Test
    public void test() {
    	Map<String, Object> startAndEnd = marketDao.getStartAndEnd(118);
    	if (startAndEnd == null) {
			System.out.println("------------------null-------------------");
		}else {
			if (startAndEnd.get("starttime") == null) {
				System.out.println("------------------null-------------------");
			}
			String aString= startAndEnd.get("starttime") + "-" +  startAndEnd.get("endtime");
			System.out.println(aString);
		}
}
}