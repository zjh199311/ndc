package com.zhongjian.dao.cart;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartParamDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 菜品id
     */
    private Integer gid;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户id
     */
    private Integer sid;

    /**
     * 创建时间
     */
    private Integer ctime;
}
