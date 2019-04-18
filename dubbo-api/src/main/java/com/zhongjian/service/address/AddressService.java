package com.zhongjian.service.address;

import com.zhongjian.dto.cart.address.query.CartAddressQueryDTO;
import com.zhongjian.dto.cart.address.result.CartAddressResultDTO;

/**
 * @Author: ldd
 */
public interface AddressService {

    /**
     * 获取默认地址0表示默认
     * @param cartAddressQueryDTO
     * @return
     */
    CartAddressResultDTO previewOrderAddress(CartAddressQueryDTO cartAddressQueryDTO);


    void updateDefaultAddress(CartAddressQueryDTO cartAddressQueryDTO);

    void updateUserMarketIdById(CartAddressQueryDTO cartAddressQueryDTO);



}
