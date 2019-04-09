package com.zhongjian.service.hm.basket.impl;

import com.zhongjian.dao.entity.hm.basket.HmBasketBean;
import com.zhongjian.dao.entity.hm.goods.HmGoodsBean;
import com.zhongjian.dao.entity.hm.user.HmUserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dao.hm.HmBasketParamDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.hm.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.basket.result.HmBasketResultDTO;
import com.zhongjian.service.hm.basket.HmBasketService;
import com.zhongjian.util.LogUtil;
import com.zhongjian.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

/**
 * @Author: ldd
 */
@Service("hmBasketService")
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
    public ResultDTO<Object> addOrUpdateInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO) {
        //参数校验
        if (null == hmBasketEditQueryDTO) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        if (null == hmBasketEditQueryDTO.getAmount() || BigDecimal.ZERO.equals(new BigDecimal(hmBasketEditQueryDTO.getAmount()))) {
            return ResultUtil.getFail(CommonMessageEnum.AMOUNT_IS_NULL);
        }
        if (null == hmBasketEditQueryDTO.getGid()) {
            return ResultUtil.getFail(CommonMessageEnum.GID_IS_NULL);
        }
        if (null == hmBasketEditQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }

        //根据前端传入的商品id去查询pid,
        HmGoodsBean hmGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(hmBasketEditQueryDTO.getGid());
        //总价
        BigDecimal multiply = hmGoodsBean.getPrice().multiply(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
        //根据gid,pid,uid查询购物车信息.
        HmBasketParamDTO queryDTO = new HmBasketParamDTO();
        queryDTO.setGid(hmBasketEditQueryDTO.getGid());
        queryDTO.setSid(hmGoodsBean.getPid());
        queryDTO.setUid(hmBasketEditQueryDTO.getUid());
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
        if (null == findBasketBeanById) {
            hmBasketBean.setAmount(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
            hmBasketBean.setUnitprice(hmGoodsBean.getPrice());
            //计算总价保留两个小数点
            hmBasketBean.setPrice(BigDecimal.valueOf(multiply.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            this.dao.insertSelective(hmBasketBean);
        } else {
            hmBasketBean.setId(findBasketBeanById.getId());
            //获取原本的总价.
            BigDecimal unitprice = findBasketBeanById.getPrice();
            //计算新的总价(原本总价+单价乘价格)
            hmBasketBean.setPrice(BigDecimal.valueOf(multiply.add(unitprice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            hmBasketBean.setAmount(findBasketBeanById.getAmount().add(new BigDecimal(hmBasketEditQueryDTO.getAmount())));
            hmBasketBean.setUnitprice(hmGoodsBean.getPrice());
            this.dao.updateByPrimaryKeySelective(hmBasketBean);
        }
        return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
    }

    @Override
    public ResultDTO<Object> queryList(HmBasketListQueryDTO hmBasketListQueryDTO) {
        if (null == hmBasketListQueryDTO) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }

        if (null == hmBasketListQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }

        if (null == hmBasketListQueryDTO.getSid()) {
            return ResultUtil.getFail(CommonMessageEnum.SID_IS_NULL);
        }
        List<HmBasketResultDTO> selectBasketBeanById = this.dao.executeListMethod(hmBasketListQueryDTO, "selectBasketBeanById", HmBasketResultDTO.class);
        StringBuffer stringBuffer;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        for (HmBasketResultDTO hmBasketResultDTO : selectBasketBeanById) {
            stringBuffer = new StringBuffer();
            //数量
            String amount = hmBasketResultDTO.getAmount();
            //decimalFormat格式转换
            stringBuffer.append(decimalFormat.format(Double.parseDouble(amount))).append("斤");
            hmBasketResultDTO.setAmount(stringBuffer.toString());
            hmBasketResultDTO.setTotalPrice(hmBasketResultDTO.getTotalPrice() + "元");
        }
        return ResultUtil.getSuccess(selectBasketBeanById);
    }

    @Override
    public ResultDTO<Object> deleteInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO) {

        if (null == hmBasketDelQueryDTO) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        if (null == hmBasketDelQueryDTO.getId()) {
            return ResultUtil.getFail(CommonMessageEnum.PRI_ID_IS_EMPT);
        }
        if (null == hmBasketDelQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }
        this.dao.executeDeleteMethod(hmBasketDelQueryDTO, "deleteBeanById");

        return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
    }

    @Override
    public ResultDTO<Object> deleteAllInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO) {
        if (null == hmBasketDelQueryDTO) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        if (null == hmBasketDelQueryDTO.getSid()) {
            return ResultUtil.getFail(CommonMessageEnum.SID_IS_NULL);
        }
        if (null == hmBasketDelQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }
        HmBasketParamDTO hmBasketParamDTO = new HmBasketParamDTO();
        hmBasketParamDTO.setSid(hmBasketDelQueryDTO.getSid());
        hmBasketParamDTO.setUid(hmBasketDelQueryDTO.getUid());
        this.dao.executeDeleteMethod(hmBasketParamDTO, "deleteBeanById");

        return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
    }

    @Override
    public ResultDTO<Object> editInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO) {
        if (null == hmBasketEditQueryDTO) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        if (null == hmBasketEditQueryDTO.getAmount()) {
            return ResultUtil.getFail(CommonMessageEnum.AMOUNT_IS_NULL);
        }
        if (null == hmBasketEditQueryDTO.getGid()) {
            return ResultUtil.getFail(CommonMessageEnum.GID_IS_NULL);
        }
        if (null == hmBasketEditQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }
        //根据前端传入的商品id去查询pid,
        HmGoodsBean hmGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(hmBasketEditQueryDTO.getGid());
        if (null == hmGoodsBean) {
            LogUtil.info("根据商品id找不到商品信息", "hmGoodsBean" + hmGoodsBean);
            return ResultUtil.getFail(CommonMessageEnum.DATA_IS_EMPTY);
        } else {
            HmBasketParamDTO queryDTO = new HmBasketParamDTO();
            queryDTO.setGid(hmBasketEditQueryDTO.getGid());
            queryDTO.setSid(hmGoodsBean.getPid());
            queryDTO.setUid(hmBasketEditQueryDTO.getUid());
            HmBasketBean findBasketBeanById = this.dao.executeSelectOneMethod(queryDTO, "findBasketBeanById", HmBasketBean.class);
            //如果是页面上的减号判断如果传来的值为0则是删除操作.
            if (BigDecimal.ZERO.equals(new BigDecimal(hmBasketEditQueryDTO.getAmount()))) {

                HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
                hmBasketDelQueryDTO.setId(findBasketBeanById.getId());
                hmBasketDelQueryDTO.setUid(hmBasketEditQueryDTO.getUid());
                ResultDTO<Object> dto = deleteInfoById(hmBasketDelQueryDTO);
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
                hmBasketBean.setAmount(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
                //总价
                BigDecimal totalprice = hmGoodsBean.getPrice().multiply(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
                hmBasketBean.setPrice(totalprice);
                //获取时间用unix时间戳
                Long unixTime = System.currentTimeMillis() / 1000;
                hmBasketBean.setCtime(unixTime.intValue());
                this.dao.updateByPrimaryKeySelective(hmBasketBean);
            }
        }
        return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
    }

    @Override
    public ResultDTO<Object> deleteAllByPid(HmBasketDelQueryDTO hmBasketDelQueryDTO) {
        if (null == hmBasketDelQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }
        if (null == hmBasketDelQueryDTO.getSids() || hmBasketDelQueryDTO.getSids().length < 0) {
            return ResultUtil.getFail(CommonMessageEnum.SID_IS_NULL);
        }
        this.dao.executeDeleteMethod(hmBasketDelQueryDTO,"deleteInfoBySids");

        return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
    }
}
