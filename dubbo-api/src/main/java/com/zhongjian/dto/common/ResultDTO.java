package com.zhongjian.dto.common;


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
