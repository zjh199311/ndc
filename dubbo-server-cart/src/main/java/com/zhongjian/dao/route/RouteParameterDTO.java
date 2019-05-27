package com.zhongjian.dao.route;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 李冬冬
 */
@Data
public class RouteParameterDTO implements Serializable {

    private static final long serialVersionUID = -7482562414423713395L;

    /**
     * 方法名
     */
    private String oprType;

    /**
     * 传参的字符串(json)
     */
    private String paramData;

}
