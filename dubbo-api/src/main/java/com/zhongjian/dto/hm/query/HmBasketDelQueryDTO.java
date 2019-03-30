package com.zhongjian.dto.hm.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: yd
 */
@Data
public class HmBasketDelQueryDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * basketId
     */
    private Integer id;

    /**
     * 用户token
     */
    private String loginToken;

    /**
     * 商户id
     */
    private Integer sid;

}
