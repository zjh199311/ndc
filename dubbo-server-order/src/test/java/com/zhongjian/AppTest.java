package com.zhongjian;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhongjian.dto.order.hm.shopown.query.HmShopownStatusQueryDTO;
import com.zhongjian.service.order.hm.shopown.OrderHmShopownService;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"/META-INF/spring/dubbo-server-one.xml"})
public class AppTest {  
    
    @Resource
    private OrderHmShopownService service;
    @Test  
    public void getListTest() throws ParseException{
        HmShopownStatusQueryDTO dto = new HmShopownStatusQueryDTO();
        List<Integer> pids = new ArrayList<Integer>();
        pids.add(103);
        dto.setPids(pids);
        dto.setStatus(1);
        System.out.println(service.judgeHmShopownStatus(dto));
    }  
}
