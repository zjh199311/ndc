package com.zhongjian.dto.hm.shopown.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: ldd
 */
@Data
public class HmShopownResultDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;


    /**
     * 菜场id
     */
    private Integer id;

    /**
     * 菜场名称
     */
    private String marketName;

    /**
     * 活动内容
     */
    private String activity;

    /**
     * 全部价格
     */
    private String totalPrice;

    /**
     * 活动描述
     */
    private String rule;

    /**
     * 活动类型0满减1折扣
     */
    private Integer type;





}
