package com.zhongjian.dto.order.order.query;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OrderStatusQueryDTO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1728911295677881024L;
    
    private List<Integer> pids;
    
    private Integer status;

}
