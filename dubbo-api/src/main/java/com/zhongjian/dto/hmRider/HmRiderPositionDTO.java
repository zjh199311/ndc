package com.zhongjian.dto.hmRider;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: yd
 */
@Data
public class HmRiderPositionDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    private Integer id;

    private Integer rid;

    private String longitude;

    private String latitude;

    private Integer ctime;
}
