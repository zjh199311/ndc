package com.zhongjian.dto.cart.market.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class CartMarketResultListDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 开张
     */
    private List<CartMarketResultByOpenDTO> open;

    /**
     * 预约
     */
    private List<CartMarketResultByAdvenceDTO> advance;

    /**
     * 打烊
     */
    private List<CartMarketResultByCloseDTO> close;
}
