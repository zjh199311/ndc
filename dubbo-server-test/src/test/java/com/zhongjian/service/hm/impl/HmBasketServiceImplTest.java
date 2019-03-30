package com.zhongjian.service.hm.impl;

import com.zhongjian.dto.hm.query.HmBasketQueryDTO;
import com.zhongjian.service.HmBaseTest;
import com.zhongjian.service.hm.HmBasketService;
import org.junit.Test;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @Author: yd
 */
public class HmBasketServiceImplTest extends HmBaseTest{

    @Resource
    private HmBasketService hmBasketService;

    @Test
    public void test(){
        HmBasketQueryDTO hmBasketQueryDTO = new HmBasketQueryDTO();
        hmBasketQueryDTO.setGid(3);
        hmBasketQueryDTO.setAmount(new BigDecimal(1));
        hmBasketQueryDTO.setLoginToken("80bcb08a9db609efc7f8dd81fafefc16");
        hmBasketQueryDTO.setRemark("加辣");
        hmBasketService.addOrUpdateInfo(hmBasketQueryDTO);
}
}