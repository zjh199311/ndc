package com.zhongjian;


import com.alibaba.fastjson.JSONObject;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.service.pay.GenerateSignatureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {


    @Resource
    private GenerateSignatureService generateSignatureService;

    @Resource
    private PropUtil propUtil;


    @Test
    public void test() {
    }

}
