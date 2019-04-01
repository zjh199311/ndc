package com.zhongjian.service.hm.shopown.impl;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import com.zhongjian.service.HmBaseTest;
import com.zhongjian.service.hm.shopown.HmShopownService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @Author: yd
 */
public class HmShopownServiceImplTest extends HmBaseTest{

    @Resource
    private HmShopownService shopownService ;

    @Test
    public void test(){
        shopownService.queryList("80bcb08a9db609efc7f8dd81fafefc16");
    }

}