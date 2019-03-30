package com.zhongjian.service.hm.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.result.HmBasketResultDTO;
import com.zhongjian.service.HmBaseTest;
import com.zhongjian.service.hm.HmBasketService;
import org.junit.Test;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: yd
 */
public class HmBasketServiceImplTest extends HmBaseTest {

    @Resource
    private HmBasketService hmBasketService;

    @Test
    public void test() {
        HmBasketEditQueryDTO hmBasketEditQueryDTO = new HmBasketEditQueryDTO();
        hmBasketEditQueryDTO.setGid(1055);
        hmBasketEditQueryDTO.setAmount(new BigDecimal(3.5));
        hmBasketEditQueryDTO.setLoginToken("cb78876213d7d044a6486beba490a4bb");
        hmBasketEditQueryDTO.setRemark("");

        ResultDTO<Boolean> resultDTO = hmBasketService.addOrUpdateInfo(hmBasketEditQueryDTO);
        System.out.println(resultDTO);
    }

    @Test
    public void test1() {

        HmBasketListQueryDTO hmBasketListQueryDTO = new HmBasketListQueryDTO();
        hmBasketListQueryDTO.setLoginToken("cb78876213d7d044a6486beba490a4bb");
        hmBasketListQueryDTO.setSid(102);


        ResultDTO<List<HmBasketResultDTO>> resultDTO = hmBasketService.queryListById(hmBasketListQueryDTO);
        System.out.println(JSONObject.toJSONString(resultDTO.getData()));
    }

    @Test
    public void test2() {
        HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
        hmBasketDelQueryDTO.setId(5);
        hmBasketDelQueryDTO.setLoginToken("80bcb08a9db609efc7f8dd81fafefc16");
        hmBasketService.deleteInfoById(hmBasketDelQueryDTO);
    }


    @Test
    public void test3() {
        HmBasketEditQueryDTO hmBasketEditQueryDTO = new HmBasketEditQueryDTO();
        hmBasketEditQueryDTO.setAmount(new BigDecimal(3));
        hmBasketEditQueryDTO.setGid(1053);
        hmBasketEditQueryDTO.setLoginToken("cb78876213d7d044a6486beba490a4bb");

        hmBasketService.editInfo(hmBasketEditQueryDTO);
    }

    @Test
    public void test4() {
        HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
        hmBasketDelQueryDTO.setLoginToken("80bcb08a9db609efc7f8dd81fafefc16");
        hmBasketDelQueryDTO.setSid(102);


        hmBasketService.deleteAllInfoById(hmBasketDelQueryDTO);
    }
}