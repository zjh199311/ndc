package com.zhongjian.service.hm;

import com.zhongjian.common.dto.ResultDTO;
import com.zhongjian.dao.entity.hm.HmRiderPositionBean;

/**
 * @Author: ldd
 */
public interface HmRiderPositionService {

    ResultDTO<Boolean> insert(HmRiderPositionBean hmRiderPositionBean);
}
