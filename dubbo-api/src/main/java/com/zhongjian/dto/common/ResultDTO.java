package com.zhongjian.dto.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author: ldd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO<T> implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    private Boolean flag;

    private T data;

    private String msg;

    private int code;

    private Integer total;

}
