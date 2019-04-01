package com.zhongjian.dto.hm.basket.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class HmBasketListQueryDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 用户login_token
     */
    private String loginToken;

    /**
     * 商户id
     */
    private Integer sid;

    /**
     * 用户id
     */
    private Integer uid;
}
