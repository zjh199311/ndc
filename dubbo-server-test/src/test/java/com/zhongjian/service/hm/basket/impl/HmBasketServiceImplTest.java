package com.zhongjian.service.hm.basket.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.basket.result.HmBasketResultDTO;
import com.zhongjian.service.HmBaseTest;
import com.zhongjian.service.hm.basket.HmBasketService;
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
//        hmBasketEditQueryDTO.setAmount(new BigDecimal(3.5));
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


        ResultDTO<List<HmBasketResultDTO>> resultDTO = hmBasketService.queryList(hmBasketListQueryDTO);
        System.out.println(JSONObject.toJSONString(resultDTO.getData()));
    }

    @Test
    public void test2() {
        HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
        hmBasketDelQueryDTO.setId(3);
        hmBasketDelQueryDTO.setLoginToken("cb78876213d7d044a6486beba490a4bb");
        hmBasketService.deleteInfoById(hmBasketDelQueryDTO);
    }


    @Test
    public void test3() {
        HmBasketEditQueryDTO hmBasketEditQueryDTO = new HmBasketEditQueryDTO();
//        hmBasketEditQueryDTO.setAmount(new BigDecimal(3));
        hmBasketEditQueryDTO.setGid(1055);
        hmBasketEditQueryDTO.setLoginToken("cb78876213d7d044a6486beba490a4bb");

        hmBasketService.editInfo(hmBasketEditQueryDTO);
    }

    @Test
    public void test4() {
        HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
        hmBasketDelQueryDTO.setLoginToken("cb78876213d7d044a6486beba490a4bb");
        hmBasketDelQueryDTO.setSid(102);


        hmBasketService.deleteAllInfoById(hmBasketDelQueryDTO);
    }
}