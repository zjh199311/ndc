package com.zhongjian.service.address;

import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;

/**
 * @Author: ldd
 */
public interface AddressService {

    /**
     * 获取默认地址0表示默认
     * @param orderAddressQueryDTO
     * @return
     */
    OrderAddressResultDTO previewOrderAddress(OrderAddressQueryDTO orderAddressQueryDTO);


    void updateDefaultAddress(OrderAddressQueryDTO orderAddressQueryDTO);

    void updateUserMarketIdById(OrderAddressQueryDTO orderAddressQueryDTO);



}
