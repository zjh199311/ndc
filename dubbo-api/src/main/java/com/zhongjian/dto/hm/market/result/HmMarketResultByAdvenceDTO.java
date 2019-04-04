package com.zhongjian.dto.hm.market.result;

import com.zhongjian.dto.hm.marketActivity.result.HmMarketActivityResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultByAdvenceDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class HmMarketResultByAdvenceDTO {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 菜场id
     */
    private Integer martketId;

    /**
     * 菜场名称
     */
    private String marketName;

    /**
     * 全部价格
     */
    private String totalPrice;

    /**
     * 活动描述
     */
    private String rule;

    /**
     * 状态
     */
    private String status;

    /**
     * 用户在商户下购买的信息
     */
    private List<HmShopownResultDTO> hmShopownResultDTOS;

    /**
     * 菜场活动
     */
    HmMarketActivityResultDTO hmMarketActivityResultDTO;
}
