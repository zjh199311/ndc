package com.zhongjian.dto.hm.market.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class HmMarketResultListDTO  implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 开张
     */
    private List<HmMarketResultByOpenDTO> hmMarketResultByOpen;

    /**
     * 预约
     */
    private List<HmMarketResultByAdvenceDTO> hmMarketResultByAdvance;

    /**
     * 打烊
     */
    private List<HmMarketResultByCloseDTO> hmMarketResultByClose;
}
