package com.zhongjian;


import com.alibaba.fastjson.JSONObject;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dao.cart.CartParamDTO;
import com.zhongjian.dao.entity.cart.rider.CartRiderOrderBean;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketDelQueryDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketEditQueryDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketListQueryDTO;
import com.zhongjian.dto.cart.address.query.CartAddressQueryDTO;
import com.zhongjian.service.address.AddressService;
import com.zhongjian.service.cart.basket.CartBasketService;
import com.zhongjian.service.cart.shopown.CartShopownService;
import com.zhongjian.service.message.MessagePushService;
import com.zhongjian.service.user.UserService;
import com.zhongjian.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.annotation.Resources;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/dubbo-server.xml"})
public class AppTest {
    @Resource
    private CartBasketService hmBasketService;

    @Resource
    private CartShopownService cartShopownService;

    @Resource
    private AddressService addressService;

    @Resource
    private MessagePushService messagePushService;


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
        CartBasketEditQueryDTO hmBasketDelQueryDTO = new CartBasketEditQueryDTO();
        hmBasketDelQueryDTO.setUid(32716);
        hmBasketDelQueryDTO.setGid(0);
        hmBasketDelQueryDTO.setSid(127);
        hmBasketDelQueryDTO.setPrice("200");
        hmBasketDelQueryDTO.setAmount("4");
        hmBasketDelQueryDTO.setRemark("2");
        System.out.println(JSONObject.toJSONString(hmBasketService.addOrUpdateInfo(hmBasketDelQueryDTO)));

    }

    @Test
    public void queryList() {
        CartBasketListQueryDTO cartBasketListQueryDTO = new CartBasketListQueryDTO();
        cartBasketListQueryDTO.setUid(32716);
        cartBasketListQueryDTO.setSid(320);
        ResultDTO<Object> objectResultDTO = hmBasketService.queryList(cartBasketListQueryDTO);
        System.out.println(JSONObject.toJSONString(objectResultDTO));

    }

    @Test
    public void deleteInfoById() {
        CartBasketDelQueryDTO hmBasketListQueryDTO = new CartBasketDelQueryDTO();
        hmBasketListQueryDTO.setId(36);
        hmBasketListQueryDTO.setUid(32716);
        System.out.println(JSONObject.toJSONString(hmBasketService.deleteInfoById(hmBasketListQueryDTO)));
    }

    @Test
    public void deleteAllInfoById() {
        CartBasketDelQueryDTO hmBasketListQueryDTO = new CartBasketDelQueryDTO();
        hmBasketListQueryDTO.setSid(1);
        hmBasketListQueryDTO.setUid(1);
        System.out.println(JSONObject.toJSONString(hmBasketService.deleteAllInfoById(hmBasketListQueryDTO)));
    }

    @Test
    public void editInfo() {
        CartBasketEditQueryDTO hmBasketListQueryDTO = new CartBasketEditQueryDTO();
        hmBasketListQueryDTO.setAmount("0");
//       hmBasketListQueryDTO.setPrice("100");
        hmBasketListQueryDTO.setId(33);
        hmBasketListQueryDTO.setUid(32716);
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
    @Test
    public void user1(){
        System.out.println(userService.getUserBeanById(1));
    }
    @Test
    public void address(){
        CartAddressQueryDTO cartAddressQueryDTO = new CartAddressQueryDTO();
        cartAddressQueryDTO.setId(1);
        cartAddressQueryDTO.setUid(1);


        System.out.println(addressService.previewOrderAddress(cartAddressQueryDTO));
    }

    @Test
    public void add(){
        CartAddressQueryDTO cartAddressQueryDTO = new CartAddressQueryDTO();
        cartAddressQueryDTO.setId(1);
        cartAddressQueryDTO.setUid(1);


        System.out.println(addressService.previewOrderAddress(cartAddressQueryDTO));
    }
    @Test
    public void updateDefalut(){
        CartAddressQueryDTO cartAddressQueryDTO = new CartAddressQueryDTO();
        cartAddressQueryDTO.setId(1);
        cartAddressQueryDTO.setUid(1);


       addressService.updateDefaultAddress(cartAddressQueryDTO);
    }

    @Test
    public void updateUserMarketId(){
        CartAddressQueryDTO cartAddressQueryDTO = new CartAddressQueryDTO();
        cartAddressQueryDTO.setId(1);
        cartAddressQueryDTO.setMarketId(1);


        addressService.updateUserMarketIdById(cartAddressQueryDTO);
    }

    @Test
    public void message(){


        String  [] pid={"11111","22222","3333"};
        messagePushService.messagePush("8bd78jf97d4j","525270d2d59bb798",pid);
    }

}
