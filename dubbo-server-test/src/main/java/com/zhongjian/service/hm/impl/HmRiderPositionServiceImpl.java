package com.zhongjian.service.hm.impl;


import com.zhongjian.dao.entity.hm.HmRiderPositionBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hmRider.HmRiderPositionDTO;
import com.zhongjian.service.hmRider.HmRiderPositionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ldd
 */
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = HmRiderPositionService.class, retries = -1)
public class HmRiderPositionServiceImpl extends HmBaseService<HmRiderPositionBean, Integer> implements HmRiderPositionService {


    public ResultDTO<Boolean> insert(HmRiderPositionDTO hmRiderPositionDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        HmRiderPositionBean hmRiderPositionBean = new HmRiderPositionBean();
        BeanUtils.copyProperties(hmRiderPositionDTO, hmRiderPositionBean);
        int insert = this.dao.insert(hmRiderPositionBean);
        if (insert > 0) {
            resultDTO.setFlag(true);
        }
        return resultDTO;
    }

}
