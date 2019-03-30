package com.zhongjian.service.hm.impl;

import com.zhongjian.common.constant.FinalDatas;
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
import com.zhongjian.dto.hm.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.result.HmBasketResultDTO;
import com.zhongjian.service.hm.HmBasketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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
    public ResultDTO<Boolean> addOrUpdateInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        //参数校验
        if (null == hmBasketEditQueryDTO) {
            resultDTO.setErrorMessage(CommonMessageEnum.PARAM_LOST.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.PARAM_LOST.getCode());
            return resultDTO;
        }
        if (null == hmBasketEditQueryDTO.getAmount() || new BigDecimal(0).equals(hmBasketEditQueryDTO.getAmount())) {
            resultDTO.setErrorMessage(HmBasketEnum.AMOUNT_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.AMOUNT_IS_NULL.getCode());
            return resultDTO;
        }
        if (null == hmBasketEditQueryDTO.getGid()) {
            resultDTO.setErrorMessage(HmBasketEnum.GID_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.GID_IS_NULL.getCode());
            return resultDTO;
        }
        if (StringUtils.isBlank(hmBasketEditQueryDTO.getLoginToken())) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKEN_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKEN_IS_NULL.getCode());
            return resultDTO;
        }

        //根据前端传入的商品id去查询pid,
        HmGoodsBean hmGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(hmBasketEditQueryDTO.getGid());
        //总价
        BigDecimal multiply = hmGoodsBean.getPrice().multiply(hmBasketEditQueryDTO.getAmount());
        //获取uid
        Integer uid = this.hmUserBeanHmDAO.executeSelectOneMethod(hmBasketEditQueryDTO.getLoginToken(), "findUidByLoginToken", Integer.class);

        //根据gid,pid,uid查询购物车信息.
        HmBasketParamDTO queryDTO = new HmBasketParamDTO();
        queryDTO.setGid(hmBasketEditQueryDTO.getGid());
        queryDTO.setSid(hmGoodsBean.getPid());
        queryDTO.setUid(uid);
        HmBasketBean findBasketBeanById = this.dao.executeSelectOneMethod(queryDTO, "findBasketBeanById", HmBasketBean.class);
        //这边判断要是查询出来为空则为新增要是有数据则为更改
        HmBasketBean hmBasketBean = new HmBasketBean();
        BeanUtils.copyProperties(queryDTO, hmBasketBean);
        if (StringUtil.isBlank(hmBasketEditQueryDTO.getRemark())) {
            hmBasketBean.setRemark("");
        } else {
            hmBasketBean.setRemark(hmBasketEditQueryDTO.getRemark());
        }
        //获取时间用unix时间戳
        Long unixTime = System.currentTimeMillis() / 1000;
        hmBasketBean.setCtime(unixTime.intValue());
        int i;
        if (null == findBasketBeanById) {
            hmBasketBean.setAmount(hmBasketEditQueryDTO.getAmount());
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
            hmBasketBean.setAmount(findBasketBeanById.getAmount().add(hmBasketEditQueryDTO.getAmount()));
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

    @Override
    public ResultDTO<List<HmBasketResultDTO>> queryListById(HmBasketListQueryDTO hmBasketListQueryDTO) {
        ResultDTO<List<HmBasketResultDTO>> resultDTO = new ResultDTO<List<HmBasketResultDTO>>();
        resultDTO.setFlag(false);
        if (null == hmBasketListQueryDTO) {
            resultDTO.setErrorMessage(CommonMessageEnum.PARAM_LOST.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.PARAM_LOST.getCode());
            return resultDTO;
        }

        if (StringUtils.isBlank(hmBasketListQueryDTO.getLoginToken())) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKEN_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKEN_IS_NULL.getCode());
            return resultDTO;
        }

        if (null == hmBasketListQueryDTO.getSid()) {
            resultDTO.setErrorMessage(HmBasketEnum.SID_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.SID_IS_NULL.getCode());
            return resultDTO;
        }

        //获取uid
        Integer uid = this.hmUserBeanHmDAO.executeSelectOneMethod(hmBasketListQueryDTO.getLoginToken(), "findUidByLoginToken", Integer.class);

        HmBasketParamDTO hmBasketParamDTO = new HmBasketParamDTO();
        hmBasketParamDTO.setSid(hmBasketListQueryDTO.getSid());
        hmBasketParamDTO.setUid(uid);

        List<HmBasketResultDTO> selectBasketBeanById = this.dao.executeListMethod(hmBasketParamDTO, "selectBasketBeanById", HmBasketResultDTO.class);
        resultDTO.setData(selectBasketBeanById);
        resultDTO.setFlag(true);
        resultDTO.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
        resultDTO.setStatusCode(CommonMessageEnum.SUCCESS.getCode());

        return resultDTO;
    }

    @Override
    public ResultDTO<Boolean> deleteInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();

        if (null == hmBasketDelQueryDTO) {
            resultDTO.setErrorMessage(CommonMessageEnum.PARAM_LOST.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.PARAM_LOST.getCode());
            return resultDTO;
        }
        if (null == hmBasketDelQueryDTO.getId()) {
            resultDTO.setErrorMessage(CommonMessageEnum.PRI_ID_IS_EMPT.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.PRI_ID_IS_EMPT.getCode());
            return resultDTO;
        }
        if (StringUtils.isBlank(hmBasketDelQueryDTO.getLoginToken())) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKEN_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKEN_IS_NULL.getCode());
            return resultDTO;
        }
        //获取uid
        Integer uid = this.hmUserBeanHmDAO.executeSelectOneMethod(hmBasketDelQueryDTO.getLoginToken(), "findUidByLoginToken", Integer.class);

        HmBasketParamDTO hmBasketParamDTO = new HmBasketParamDTO();
        hmBasketParamDTO.setId(hmBasketDelQueryDTO.getId());
        hmBasketParamDTO.setUid(uid);
        int i = this.dao.executeDeleteMethod(hmBasketParamDTO, "deleteBeanById");
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

    @Override
    public ResultDTO<Boolean> deleteAllInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        if (null == hmBasketDelQueryDTO) {
            resultDTO.setErrorMessage(CommonMessageEnum.PARAM_LOST.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.PARAM_LOST.getCode());
            return resultDTO;
        }
        if (null == hmBasketDelQueryDTO.getSid()) {
            resultDTO.setErrorMessage(HmBasketEnum.SID_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.SID_IS_NULL.getCode());
            return resultDTO;
        }
        if (StringUtils.isBlank(hmBasketDelQueryDTO.getLoginToken())) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKEN_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKEN_IS_NULL.getCode());
            return resultDTO;
        }
        //获取uid
        Integer uid = this.hmUserBeanHmDAO.executeSelectOneMethod(hmBasketDelQueryDTO.getLoginToken(), "findUidByLoginToken", Integer.class);
        HmBasketParamDTO hmBasketParamDTO = new HmBasketParamDTO();
        hmBasketParamDTO.setSid(hmBasketDelQueryDTO.getSid());
        hmBasketParamDTO.setUid(uid);
        int i = this.dao.executeDeleteMethod(hmBasketParamDTO, "deleteBeanById");
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

    @Override
    public ResultDTO<Boolean> editInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);

        if (null == hmBasketEditQueryDTO) {
            resultDTO.setErrorMessage(CommonMessageEnum.PARAM_LOST.getMsg());
            resultDTO.setStatusCode(CommonMessageEnum.PARAM_LOST.getCode());
            return resultDTO;
        }
        if (null == hmBasketEditQueryDTO.getAmount()) {
            resultDTO.setErrorMessage(HmBasketEnum.AMOUNT_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.AMOUNT_IS_NULL.getCode());
            return resultDTO;
        }
        if (null == hmBasketEditQueryDTO.getGid()) {
            resultDTO.setErrorMessage(HmBasketEnum.GID_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.GID_IS_NULL.getCode());
            return resultDTO;
        }
        if (StringUtils.isBlank(hmBasketEditQueryDTO.getLoginToken())) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKEN_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKEN_IS_NULL.getCode());
            return resultDTO;
        }
        //根据前端传入的商品id去查询pid,
        HmGoodsBean hmGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(hmBasketEditQueryDTO.getGid());
        //获取uid
        Integer uid = this.hmUserBeanHmDAO.executeSelectOneMethod(hmBasketEditQueryDTO.getLoginToken(), "findUidByLoginToken", Integer.class);
        HmBasketParamDTO queryDTO = new HmBasketParamDTO();
        queryDTO.setGid(hmBasketEditQueryDTO.getGid());
        queryDTO.setSid(hmGoodsBean.getPid());
        queryDTO.setUid(uid);
        HmBasketBean findBasketBeanById = this.dao.executeSelectOneMethod(queryDTO, "findBasketBeanById", HmBasketBean.class);
        //如果是页面上的减号判断如果传来的值为0则是删除操作.
        if (new BigDecimal(0).equals(hmBasketEditQueryDTO.getAmount())) {

            HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
            hmBasketDelQueryDTO.setId(findBasketBeanById.getId());
            hmBasketDelQueryDTO.setLoginToken(hmBasketEditQueryDTO.getLoginToken());
            ResultDTO<Boolean> dto = deleteInfoById(hmBasketDelQueryDTO);
            return dto;
        } else {
            //否则就是编辑操作重新该单并替换原有的订单
            HmBasketBean hmBasketBean = new HmBasketBean();
            BeanUtils.copyProperties(queryDTO, hmBasketBean);
            hmBasketBean.setId(findBasketBeanById.getId());
            if (StringUtil.isBlank(hmBasketEditQueryDTO.getRemark())) {
                hmBasketBean.setRemark("");
            } else {
                hmBasketBean.setRemark(hmBasketEditQueryDTO.getRemark());
            }
            hmBasketBean.setUnitprice(hmGoodsBean.getPrice());
            hmBasketBean.setAmount(hmBasketEditQueryDTO.getAmount());
            //总价
            BigDecimal totalprice = hmBasketEditQueryDTO.getAmount().multiply(hmGoodsBean.getPrice());
            hmBasketBean.setPrice(totalprice);
            //获取时间用unix时间戳
            Long unixTime = System.currentTimeMillis() / 1000;
            hmBasketBean.setCtime(unixTime.intValue());
            int i = this.dao.updateByPrimaryKeySelective(hmBasketBean);
            if (i > 0) {
                resultDTO.setFlag(true);
                resultDTO.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
                resultDTO.setStatusCode(CommonMessageEnum.SUCCESS.getCode());
            } else {
                resultDTO.setErrorMessage(CommonMessageEnum.FAIL.getMsg());
                resultDTO.setStatusCode(CommonMessageEnum.FAIL.getCode());
            }
        }
        return resultDTO;
    }
}
