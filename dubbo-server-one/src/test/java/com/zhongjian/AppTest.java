package com.zhongjian;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"/META-INF/spring/dubbo-server-one.xml"})
public class AppTest {  
    @Test  
    public void getListTest() throws ParseException{
    }  
}
