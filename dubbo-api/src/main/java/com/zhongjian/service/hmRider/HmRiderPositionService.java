package com.zhongjian.service.hmRider;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hmRider.HmRiderPositionDTO;

/**
 * @Author: ldd
 */
public interface HmRiderPositionService {

    ResultDTO<Boolean> insert(HmRiderPositionDTO hmRiderPositionDTO);
}
