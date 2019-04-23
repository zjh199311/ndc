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
    /**
     * 修改默认地址
     * @param cartAddressQueryDTO
     */
    void updateDefaultAddress(CartAddressQueryDTO cartAddressQueryDTO);

    /**
     * 更改marketId
     * @param cartAddressQueryDTO
     */
    void updateUserMarketIdById(CartAddressQueryDTO cartAddressQueryDTO);



}
