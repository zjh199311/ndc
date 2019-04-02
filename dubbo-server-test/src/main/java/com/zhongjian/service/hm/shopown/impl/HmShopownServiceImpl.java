package com.zhongjian.service.hm.shopown.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.common.constant.enums.hm.basket.HmBasketEnum;
import com.zhongjian.common.util.LogUtil;
import com.zhongjian.dao.entity.hm.market.HmMarketBean;
import com.zhongjian.dao.entity.hm.user.HmUserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import com.zhongjian.service.hm.shopown.HmShopownService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: ldd
 */
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = HmShopownService.class,retries = 0)
public class HmShopownServiceImpl extends HmBaseService<HmMarketBean, Integer> implements HmShopownService {

    private HmDAO<HmUserBean, Integer> hmDAO;

    @Resource
    private void setHmDAO(HmDAO<HmUserBean, Integer> hmDAO) {
        this.hmDAO = hmDAO;
        this.hmDAO.setPerfix(HmUserBean.class.getName());
    }


    @Override
    public ResultDTO<HmShopownResultDTO> queryList(String loginToken) {
        ResultDTO<HmShopownResultDTO> resultDTO = new ResultDTO<HmShopownResultDTO>();
        resultDTO.setFlag(false);
        if (StringUtils.isBlank(loginToken)) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKEN_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKEN_IS_NULL.getCode());
            return resultDTO;
        }
        //获取uid
        Integer uid = this.hmDAO.executeSelectOneMethod(loginToken, "findUidByLoginToken", Integer.class);
        LogUtil.info("获取uid", "uid:" + uid);

        List<HmShopownResultDTO> findMarketByUid = this.dao.executeListMethod(uid, "findMarketByUid", HmShopownResultDTO.class);
        StringBuffer stringBuffer = new StringBuffer();
        for (HmShopownResultDTO hmShopownResultDTO : findMarketByUid) {
            if(FinalDatas.ZERO.equals(hmShopownResultDTO.getType())){
                String[] split = hmShopownResultDTO.getRule().split("-");
                stringBuffer.append("首单购买满").append(split[0]).append("减").append(split[1]);
                hmShopownResultDTO.setRule(stringBuffer.toString());
            }
        }
        System.out.println(JSONObject.toJSONString(findMarketByUid));

        return null;
    }


}
