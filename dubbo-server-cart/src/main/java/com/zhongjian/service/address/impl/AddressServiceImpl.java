package com.zhongjian.service.address.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.cart.address.CartAddressBean;
import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.cart.address.query.CartAddressQueryDTO;
import com.zhongjian.dto.cart.address.result.CartAddressResultDTO;
import com.zhongjian.service.address.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: ldd
 */
@Service("addressService")
public class AddressServiceImpl extends HmBaseService<CartAddressBean, Integer> implements AddressService {

    private HmDAO<UserBean,Integer>hmDAO;

    @Resource
    private void setHmDAO(HmDAO<UserBean,Integer>hmDAO){
        this.hmDAO=hmDAO;
        this.hmDAO.setPerfix(UserBean.class.getName());

    }


    @Override
    public CartAddressResultDTO previewOrderAddress(CartAddressQueryDTO cartAddressQueryDTO) {
        /**
         * 要是传来的id为0.则根据uid去查询数据库并根据status为1的默认地址返回.limit=1 要是传来的id不为0,则根据id去查询在返回
         */
        if (FinalDatas.ZERO == cartAddressQueryDTO.getId()) {
            CartAddressResultDTO cartAddressResultDTO = this.dao.executeSelectOneMethod(
                    cartAddressQueryDTO.getUid(), "findAddressByUid", CartAddressResultDTO.class);
            return cartAddressResultDTO;
        } else {
            CartAddressResultDTO findAddressByid = this.dao.executeSelectOneMethod(
                    cartAddressQueryDTO.getId(), "findAddressByid", CartAddressResultDTO.class);
            return findAddressByid;
        }
    }

    @Override
    public void updateDefaultAddress(CartAddressQueryDTO cartAddressQueryDTO) {

        this.dao.executeUpdateMethod(cartAddressQueryDTO, "updateStatusByUid");
    }

    @Override
    public void updateUserMarketIdById(CartAddressQueryDTO cartAddressQueryDTO) {

        this.hmDAO.executeUpdateMethod(cartAddressQueryDTO,"updateMarketIdById");

    }
}
