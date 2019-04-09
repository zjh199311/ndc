package com.zhongjian;


import com.alibaba.fastjson.JSONObject;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.market.result.HmMarketResultListDTO;
import com.zhongjian.service.hm.shopown.HmShopownService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/dubbo-server-one.xml"})
public class AppTest {

    @Resource
    private HmShopownService shopownService;

    @Test
    public void test() {
//        ResultDTO<HmMarketResultListDTO> resultDTOResultDTO = shopownService.queryList("ac954d3bdee77aff2b0c4a809037673a");
//        System.out.println(JSONObject.toJSONString(resultDTOResultDTO.getData()));
    }


}
