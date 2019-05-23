package com.zhongjian.dto.cart.cvstore.result;

import com.zhongjian.dto.cart.shopown.result.CartShopownResultDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class CvstoreList implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    //便利店打烊
    List<CartShopownResultDTO> close;

    //便利店开张
    List<CartShopownResultDTO> Open;

}
