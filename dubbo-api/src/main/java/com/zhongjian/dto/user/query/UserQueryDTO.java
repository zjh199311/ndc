package com.zhongjian.dto.user.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class UserQueryDTO implements Serializable{

    private static final long serialVersionUID = -1728911295677881024L;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 菜场id
     */
    private Integer marketId;


    /**
     * 金额
     */
    private String price;


}
