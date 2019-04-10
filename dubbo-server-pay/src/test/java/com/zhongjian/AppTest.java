package com.zhongjian;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhongjian.dto.order.shopown.query.HmShopownStatusQueryDTO;
import com.zhongjian.service.order.shopown.OrderHmShopownService;
import com.zhongjian.service.pay.GenerateSignatureService;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"/META-INF/spring/dubbo-server-one.xml"})
public class AppTest {  
    
	@Autowired
	private GenerateSignatureService genereSignatatureService;
    @Resource
    private OrderHmShopownService service;
    @Test  
    public void getListTest() throws ParseException{
        try {
            HmShopownStatusQueryDTO dto = new HmShopownStatusQueryDTO();
            List<Integer> pids = new ArrayList<Integer>();
            pids.add(103);
            dto.setPids(pids);
            dto.setStatus(1);
            System.out.println(service.judgeHmShopownStatus(dto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }  
}
