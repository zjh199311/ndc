package com.zhongjian.service.hm.impl;

import com.zhongjian.common.constant.enums.hm.HmBasketEnum;
import com.zhongjian.common.constant.enums.response.CommonMessageEnum;
import com.zhongjian.common.util.StringUtil;
import com.zhongjian.dao.entity.hm.HmBasketBean;
import com.zhongjian.dao.entity.hm.HmGoodsBean;
import com.zhongjian.dao.entity.hm.HmUserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dao.hm.HmBasketParamDTO;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.query.HmBasketQueryDTO;
import com.zhongjian.service.hm.HmBasketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: ldd
 */
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = HmBasketService.class, retries = 0)
public class HmBasketServiceImpl extends HmBaseService<HmBasketBean, Integer> implements HmBasketService {

    private HmDAO<HmGoodsBean, Integer> hmGoodsBeanDAO;

    private HmDAO<HmUserBean, Integer> hmUserBeanHmDAO;

    @Resource
    private void setHmGoodsBeanDAO(HmDAO<HmGoodsBean, Integer> hmGoodsBeanDAO) {
        this.hmGoodsBeanDAO = hmGoodsBeanDAO;
        this.hmGoodsBeanDAO.setPerfix(HmGoodsBean.class.getName());
    }

    @Resource
    private void setHmUserBeanHmDAO(HmDAO<HmUserBean, Integer> hmUserBeanHmDAO) {
        this.hmUserBeanHmDAO = hmUserBeanHmDAO;
        this.hmUserBeanHmDAO.setPerfix(HmUserBean.class.getName());
    }

    @Override
    public ResultDTO<Boolean> addOrUpdateInfo(HmBasketQueryDTO hmBasketQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        //参数校验
        if (null == hmBasketQueryDTO) {
            resultDTO.setErrorMessage(CommonMessageEnum.PARAM_LOST.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.PARAM_LOST.getCode());
            return resultDTO;
        }
        if (null == hmBasketQueryDTO.getAmount()) {
            resultDTO.setErrorMessage(HmBasketEnum.AMOUNTISNULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.AMOUNTISNULL.getCode());
            return resultDTO;
        }
        if (null == hmBasketQueryDTO.getGid()) {
            resultDTO.setErrorMessage(HmBasketEnum.GIDISNULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.GIDISNULL.getCode());
            return resultDTO;
        }
        if (StringUtils.isBlank(hmBasketQueryDTO.getLoginToken())) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKENISNULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKENISNULL.getCode());
            return resultDTO;
        }

        //根据前端传入的商品id去查询pid,
        HmGoodsBean hmGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(hmBasketQueryDTO.getGid());
        //总价
        BigDecimal multiply = hmGoodsBean.getPrice().multiply(hmBasketQueryDTO.getAmount());
        //获取uid
        Integer uid = this.hmUserBeanHmDAO.executeSelectOneMethod(hmBasketQueryDTO.getLoginToken(), "findUidByLoginToken", Integer.class);

        //根据gid,pid,uid查询购物车信息.
        HmBasketParamDTO queryDTO = new HmBasketParamDTO();
        queryDTO.setGid(hmBasketQueryDTO.getGid());
        queryDTO.setSid(hmGoodsBean.getPid());
        queryDTO.setUid(uid);
        HmBasketBean findBasketBeanById = this.dao.executeSelectOneMethod(queryDTO, "findBasketBeanById", HmBasketBean.class);
        //这边判断要是查询出来为空则为新增要是有数据则为更改
        HmBasketBean hmBasketBean = new HmBasketBean();
        BeanUtils.copyProperties(queryDTO, hmBasketBean);
        if(StringUtil.isBlank(hmBasketQueryDTO.getRemark())){
            hmBasketBean.setRemark("");
        }else{
            hmBasketBean.setRemark(hmBasketQueryDTO.getRemark());
        }
        //获取时间用unix时间戳
        Long unixTime = System.currentTimeMillis() / 1000;
        hmBasketBean.setCtime(unixTime.intValue());
        int i;
        if (null == findBasketBeanById) {
            hmBasketBean.setAmount(hmBasketQueryDTO.getAmount());
            hmBasketBean.setUnitprice(hmGoodsBean.getPrice());
            //计算总价保留两个小数点
            hmBasketBean.setPrice(BigDecimal.valueOf(multiply.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            i = this.dao.insertSelective(hmBasketBean);
        } else {
            hmBasketBean.setId(findBasketBeanById.getId());
            //获取原本的总价.
            BigDecimal unitprice = findBasketBeanById.getPrice();
            //计算新的总价(原本总价+单价乘价格)
            hmBasketBean.setPrice(BigDecimal.valueOf(multiply.add(unitprice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            hmBasketBean.setAmount(findBasketBeanById.getAmount().add(hmBasketQueryDTO.getAmount()));
            hmBasketBean.setUnitprice(hmGoodsBean.getPrice());
            i = this.dao.updateByPrimaryKeySelective(hmBasketBean);
        }
        if (i > 0) {
            resultDTO.setFlag(true);
            resultDTO.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.SUCCESS.getCode());
        } else {
            resultDTO.setErrorMessage(CommonMessageEnum.FAIL.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.FAIL.getCode());
        }
        return resultDTO;
    }
}
