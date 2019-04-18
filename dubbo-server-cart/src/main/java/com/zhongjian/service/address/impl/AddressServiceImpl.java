package com.zhongjian.service.address.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.cart.address.CartAddressBean;
import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;
import com.zhongjian.service.address.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: ldd
 */
@Service
public class AddressServiceImpl extends HmBaseService<CartAddressBean, Integer> implements AddressService {

    private HmDAO<UserBean,Integer>hmDAO;

    @Resource
    private void setHmDAO(HmDAO<UserBean,Integer>hmDAO){
        this.hmDAO=hmDAO;
        this.hmDAO.setPerfix(UserBean.class.getName());

    }


    @Override
    public OrderAddressResultDTO previewOrderAddress(OrderAddressQueryDTO orderAddressQueryDTO) {
        /**
         * 要是传来的id为0.则根据uid去查询数据库并根据status为1的默认地址返回.limit=1 要是传来的id不为0,则根据id去查询在返回
         */
        if (FinalDatas.ZERO == orderAddressQueryDTO.getId()) {
            OrderAddressResultDTO orderAddressResultDTO = this.dao.executeSelectOneMethod(
                    orderAddressQueryDTO.getUid(), "findAddressByUid", OrderAddressResultDTO.class);
            return orderAddressResultDTO;
        } else {
            OrderAddressResultDTO findAddressByid = this.dao.executeSelectOneMethod(
                    orderAddressQueryDTO.getId(), "findAddressByid", OrderAddressResultDTO.class);
            return findAddressByid;
        }
    }

    @Override
    public void updateDefaultAddress(OrderAddressQueryDTO orderAddressQueryDTO) {

        this.dao.executeUpdateMethod(orderAddressQueryDTO, "updateStatusByUid");
    }

    @Override
    public void updateUserMarketIdById(OrderAddressQueryDTO orderAddressQueryDTO) {

        this.hmDAO.executeUpdateMethod(orderAddressQueryDTO,"updateMarketIdById");

    }
}
