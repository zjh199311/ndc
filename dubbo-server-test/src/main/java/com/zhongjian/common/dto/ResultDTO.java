package com.zhongjian.common.dto;

import lombok.Data;

/**
 *
 * @author: ldd
 */
@Data
public class ResultDTO<T> {

    private Boolean flag;

    private T data;

    private String errorMessage;

    private String statusCode;

    private Integer count;

}
