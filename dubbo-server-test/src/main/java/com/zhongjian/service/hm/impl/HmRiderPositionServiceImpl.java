package com.zhongjian.service.hm.impl;

import com.zhongjian.common.dto.ResultDTO;
import com.zhongjian.dao.entity.hm.HmRiderPositionBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.service.hm.HmRiderPositionService;
import org.springframework.stereotype.Service;

/**
 * @Author: ldd
 */
@Service
public class HmRiderPositionServiceImpl extends HmBaseService<HmRiderPositionBean, Integer> implements HmRiderPositionService {

    @Override
    public ResultDTO<Boolean> insert(HmRiderPositionBean hmRiderPositionBean) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        int insert = this.dao.insert(hmRiderPositionBean);
        if (insert > 0) {
            resultDTO.setFlag(true);
        }
        return resultDTO;
    }
}
