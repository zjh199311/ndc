package com.zhongjian;


import com.alibaba.fastjson.JSONObject;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dao.cart.CartParamDTO;
import com.zhongjian.dao.entity.cart.rider.CartRiderOrderBean;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketListQueryDTO;
import com.zhongjian.service.cart.basket.CartBasketService;
import com.zhongjian.service.cart.shopown.CartShopownService;
import com.zhongjian.service.user.UserService;
import com.zhongjian.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {
    @Resource
    private CartBasketService hmBasketService;

    @Resource
    private CartShopownService cartShopownService;

    @Resource
    private UserService userService;

    private HmDAO<CartRiderOrderBean,Integer>hmDAO;



    @Resource
    public void setHmDAO(HmDAO<CartRiderOrderBean,Integer>hmDAO){
        this.hmDAO=hmDAO;
        this.hmDAO.setPerfix(CartRiderOrderBean.class.getName());
    }



    @Resource
    private PropUtil propUtil;


    @Test
    public void addOrUpdateInfo() {
        HmBasketEditQueryDTO hmBasketDelQueryDTO = new HmBasketEditQueryDTO();
        hmBasketDelQueryDTO.setUid(32716);
        hmBasketDelQueryDTO.setGid(2684);
        hmBasketDelQueryDTO.setSid(152);
//        hmBasketDelQueryDTO.setPrice("200");
       hmBasketDelQueryDTO.setAmount("2");
        System.out.println(JSONObject.toJSONString(hmBasketService.addOrUpdateInfo(hmBasketDelQueryDTO)));

    }

    @Test
    public void queryList() {
        HmBasketListQueryDTO hmBasketListQueryDTO = new HmBasketListQueryDTO();
        hmBasketListQueryDTO.setUid(1);
        hmBasketListQueryDTO.setSid(196);
        ResultDTO<Object> objectResultDTO = hmBasketService.queryList(hmBasketListQueryDTO);
        System.out.println(JSONObject.toJSONString(objectResultDTO));

    }

    @Test
    public void deleteInfoById() {
        HmBasketDelQueryDTO hmBasketListQueryDTO = new HmBasketDelQueryDTO();
        hmBasketListQueryDTO.setId(22);
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
//        hmBasketListQueryDTO.setPrice("200");
        hmBasketListQueryDTO.setId(19);
        System.out.println(JSONObject.toJSONString(hmBasketService.editInfo(hmBasketListQueryDTO)));
    }
    @Test
    public void queryList1() {
        System.out.println(JSONObject.toJSONString(cartShopownService.queryList(32716)));
    }

    @Test
    public void user(){
        Integer uidByLoginToken = userService.getUidByLoginToken("ac954d3bdee77aff2b0c4a809037673a");
        System.out.println( uidByLoginToken);
    }
    @Test
    public void order(){

        CartParamDTO cartParamDTO = new CartParamDTO();
        Long todayZeroTime = DateUtil.getTodayZeroTime();
        cartParamDTO.setCtime(todayZeroTime.intValue());
        cartParamDTO.setUid(32716);
        Integer findCountByUid = this.hmDAO.executeSelectOneMethod(cartParamDTO, "findCountByUid", Integer.class);
        System.out.println(findCountByUid);
    }



}
