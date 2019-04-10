package com.zhongjian;


import com.alibaba.fastjson.JSONObject;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dto.hm.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketListQueryDTO;
import com.zhongjian.service.hm.basket.HmBasketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {
    @Resource
    private HmBasketService hmBasketService;

    @Resource
    private PropUtil propUtil;


    @Test
    public void addOrUpdateInfo() {
        HmBasketEditQueryDTO hmBasketDelQueryDTO = new HmBasketEditQueryDTO();
        hmBasketDelQueryDTO.setUid(1);
        hmBasketDelQueryDTO.setGid(5254);
        hmBasketDelQueryDTO.setAmount("1");
        System.out.println(JSONObject.toJSONString(hmBasketService.addOrUpdateInfo(hmBasketDelQueryDTO)));

    }

    @Test
    public void queryList() {
        HmBasketListQueryDTO hmBasketListQueryDTO = new HmBasketListQueryDTO();
        hmBasketListQueryDTO.setUid(1);
        hmBasketListQueryDTO.setSid(229);

        System.out.println(JSONObject.toJSONString(hmBasketService.queryList(hmBasketListQueryDTO)));

    }

    @Test
    public void deleteInfoById() {
        HmBasketDelQueryDTO hmBasketListQueryDTO = new HmBasketDelQueryDTO();
        hmBasketListQueryDTO.setId(11);
        hmBasketListQueryDTO.setUid(1);
        System.out.println(JSONObject.toJSONString(hmBasketService.deleteInfoById(hmBasketListQueryDTO)));
    }

    @Test
    public void deleteAllInfoById() {
        HmBasketDelQueryDTO hmBasketListQueryDTO = new HmBasketDelQueryDTO();
        hmBasketListQueryDTO.setSid(1);
        hmBasketListQueryDTO.setUid(1);
        System.out.println(JSONObject.toJSONString(hmBasketService.deleteAllInfoById(hmBasketListQueryDTO)));
    }

    @Test
    public void editInfo() {
        HmBasketEditQueryDTO hmBasketListQueryDTO = new HmBasketEditQueryDTO();
        hmBasketListQueryDTO.setAmount("0");
        hmBasketListQueryDTO.setGid(5254);
        hmBasketListQueryDTO.setUid(1);
        System.out.println(JSONObject.toJSONString(hmBasketService.editInfo(hmBasketListQueryDTO)));
    }


}
