package com.zhongjian;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dto.hm.storeActivity.result.HmStoreActivityResultDTO;



@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {  
    @Autowired
    private OrderDao orderDao;
    
    @Test
    public void test() {
//    	HmStoreActivityResultDTO storeActivtiy = orderDao.getStoreActivtiy(529,new BigDecimal(10));
//    	if (storeActivtiy == null) {
//    		System.out.println("23321321321");
//		}else {
//			System.out.println("5555555");
//		}
     List<Map<String, Object>> basketByUidAndSid = orderDao.getBasketByUidAndSid(530, 32715);
     if (basketByUidAndSid.size() == 0) {
		System.out.println(0000000);
	}
//    	System.out.println((String)basketByUidAndSid.get(1).get("price"));
    }
//  

    public static void main(String[] args) {
		BigDecimal aBigDecimal = new BigDecimal(10);
		aBigDecimal.add(new BigDecimal(1));
		System.out.println(aBigDecimal);
	}
}
