package com.zhongjian.dto.hm.storeActivity.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class HmStoreActivityResultDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 优惠数值
     */
    private String reduce;

    /**
     * 满减额度
     */
    private String full;

    /**
     * 折扣'
     */
    private String discount;

    /**
     * '类型0满减1折扣'
     */
    private Integer type;

}
