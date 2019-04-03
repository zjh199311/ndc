package com.zhongjian.service.hm.shopown.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zhongjian.common.constant.enums.hm.shopown.HmShopownEnum;
import com.zhongjian.dao.entity.hm.shopown.HmShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import com.zhongjian.dto.order.hm.shopown.query.HmShopownStatusQueryDTO;
import com.zhongjian.service.order.hm.shopown.OrderHmShopownService;

@Service("orderHmShopownService")
public class OrderHmShopownServiceImpl extends HmBaseService<HmShopownBean, Integer>
        implements OrderHmShopownService {

    @Override
    public ResultDTO<Boolean> judgeHmShopownStatus(
            HmShopownStatusQueryDTO hmShopownStatusQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        if (null == hmShopownStatusQueryDTO.getPids()
                || 0 == hmShopownStatusQueryDTO.getPids().size()) {
            resultDTO.setErrorMessage(HmShopownEnum.PID_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmShopownEnum.PID_IS_NULL.getCode());
            return resultDTO;
        }
        if (null == hmShopownStatusQueryDTO.getStatus()) {
            resultDTO.setErrorMessage(HmShopownEnum.STATUS_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmShopownEnum.STATUS_IS_NULL.getCode());
            return resultDTO;
        }
        List<HmShopownBean> hmShopownBeans = this.dao.executeListMethod(
                hmShopownStatusQueryDTO, "selectHmShopownStatusByPids",
                HmShopownBean.class);
        // 默认返回状态匹配
        resultDTO.setFlag(true);
        resultDTO.setData(true);
        for (HmShopownBean hmShopownBean : hmShopownBeans) {
            // 如果商户状态与传入状态不匹配，返回false
            if (!hmShopownStatusQueryDTO.getStatus().equals(
                    hmShopownBean.getStatus())) {
                resultDTO.setData(false);
                break;
            }
            // 如果店铺状态为预约中，但是该店铺没有开启预约 返回false
            if (2 == hmShopownBean.getStatus()
                    && 0 == hmShopownBean.getIsAppointment()) {
                resultDTO.setData(false);
                break;
            }
            //如果店铺状态为打烊或开张并且店铺为开启预约。返回false
            if ((1 == hmShopownBean.getStatus() || 0 == hmShopownBean
                    .getStatus()) && 1 == hmShopownBean.getIsAppointment()) {
                resultDTO.setData(false);
                break;
            }
        }
        return resultDTO;
    }


}
