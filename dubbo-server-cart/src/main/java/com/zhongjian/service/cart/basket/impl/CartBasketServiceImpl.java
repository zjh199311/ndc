package com.zhongjian.service.cart.basket.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.cart.basket.CartBasketBean;
import com.zhongjian.dao.entity.cart.goods.CartGoodsBean;
import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dao.cart.CartParamDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.cart.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.cart.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.cart.basket.query.HmBasketListQueryDTO;
import com.zhongjian.dto.cart.basket.result.HmBasketResultDTO;
import com.zhongjian.service.cart.basket.CartBasketService;
import com.zhongjian.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @Author: ldd
 */
@Service("hmBasketService")
public class CartBasketServiceImpl extends HmBaseService<CartBasketBean, Integer> implements CartBasketService {

    private HmDAO<CartGoodsBean, Integer> hmGoodsBeanDAO;

    private HmDAO<UserBean, Integer> hmUserBeanHmDAO;


    @Resource
    private void setHmGoodsBeanDAO(HmDAO<CartGoodsBean, Integer> hmGoodsBeanDAO) {
        this.hmGoodsBeanDAO = hmGoodsBeanDAO;
        this.hmGoodsBeanDAO.setPerfix(CartGoodsBean.class.getName());
    }

    @Resource
    private void setHmUserBeanHmDAO(HmDAO<UserBean, Integer> hmUserBeanHmDAO) {
        this.hmUserBeanHmDAO = hmUserBeanHmDAO;
        this.hmUserBeanHmDAO.setPerfix(UserBean.class.getName());
    }

    @Override
    public ResultDTO<Object> addOrUpdateInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO) {
        //参数校验
        if (null == hmBasketEditQueryDTO) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        if (null == hmBasketEditQueryDTO.getSid()) {
            return ResultUtil.getFail(CommonMessageEnum.SID_IS_NULL);
        }
        if (null == hmBasketEditQueryDTO.getGid()) {
            return ResultUtil.getFail(CommonMessageEnum.GID_IS_NULL);
        }
        if (null == hmBasketEditQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }
        //当商品名称添加为其他的时候增加该字段
        if (FinalDatas.ZERO == hmBasketEditQueryDTO.getGid()) {
            if (StringUtils.isBlank(hmBasketEditQueryDTO.getPrice())) {
                return ResultUtil.getFail(CommonMessageEnum.PRICE_IS_NULL);
            }
        } else {
            if (null == hmBasketEditQueryDTO.getAmount() || BigDecimal.ZERO.equals(new BigDecimal(hmBasketEditQueryDTO.getAmount()))) {
                return ResultUtil.getFail(CommonMessageEnum.AMOUNT_IS_NULL);
            }
        }
        //根据前端传入的商品id去查询pid,
        CartGoodsBean cartGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(hmBasketEditQueryDTO.getGid());
        //根据gid,pid,uid查询购物车信息.
        CartParamDTO queryDTO = new CartParamDTO();
        queryDTO.setGid(hmBasketEditQueryDTO.getGid());
        queryDTO.setSid(hmBasketEditQueryDTO.getSid());
        queryDTO.setUid(hmBasketEditQueryDTO.getUid());
        CartBasketBean findBasketBeanById = this.dao.executeSelectOneMethod(queryDTO, "findBasketBeanById", CartBasketBean.class);
        //这边判断要是查询出来为空则为新增要是有数据则为更改
        CartBasketBean cartBasketBean = new CartBasketBean();
        BeanUtils.copyProperties(queryDTO, cartBasketBean);
        if (StringUtil.isBlank(hmBasketEditQueryDTO.getRemark())) {
            cartBasketBean.setRemark("");
        } else {
            cartBasketBean.setRemark(hmBasketEditQueryDTO.getRemark());
        }
        //获取时间用unix时间戳
        Long unixTime = System.currentTimeMillis() / 1000;
        cartBasketBean.setCtime(unixTime.intValue());
        if (null == findBasketBeanById) {
            if (FinalDatas.ZERO == hmBasketEditQueryDTO.getGid()) {
                cartBasketBean.setPrice(new BigDecimal(hmBasketEditQueryDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                cartBasketBean.setAmount(new BigDecimal(1));
                cartBasketBean.setUnitprice(cartBasketBean.getPrice());
                cartBasketBean.setRemark(hmBasketEditQueryDTO.getRemark());
            } else {
                cartBasketBean.setAmount(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
                cartBasketBean.setUnitprice(cartGoodsBean.getPrice());
                //计算总价保留两个小数点
                //总价
                BigDecimal multiply = cartGoodsBean.getPrice().multiply(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
                cartBasketBean.setPrice(multiply.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            this.dao.insertSelective(cartBasketBean);
        } else {
            if (FinalDatas.ZERO == hmBasketEditQueryDTO.getGid()) {
                cartBasketBean.setId(findBasketBeanById.getId());
                cartBasketBean.setCtime(unixTime.intValue());
                cartBasketBean.setPrice(new BigDecimal(hmBasketEditQueryDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                cartBasketBean.setRemark(hmBasketEditQueryDTO.getRemark());
                cartBasketBean.setUnitprice(cartBasketBean.getPrice());
            }else{
                cartBasketBean.setId(findBasketBeanById.getId());
                //计算新的总价(去good里面获取价格重新计算)
                BigDecimal add = findBasketBeanById.getAmount().add(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
                BigDecimal multiply1 = add.multiply(cartGoodsBean.getPrice());
                cartBasketBean.setPrice(multiply1.setScale(2, BigDecimal.ROUND_HALF_UP));
                cartBasketBean.setAmount(findBasketBeanById.getAmount().add(new BigDecimal(hmBasketEditQueryDTO.getAmount())));
                cartBasketBean.setUnitprice(cartGoodsBean.getPrice());
            }
            this.dao.updateByPrimaryKeySelective(cartBasketBean);

        }
        return ResultUtil.getSuccess(null);
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
        //根据用户id和商户id查找basket表中的数据.
        CartParamDTO hmBasketParamDTO = new CartParamDTO();
        hmBasketParamDTO.setSid(hmBasketListQueryDTO.getSid());
        hmBasketParamDTO.setUid(hmBasketListQueryDTO.getUid());
        List<HmBasketResultDTO> findBasketBeanById = this.dao.executeListMethod(hmBasketParamDTO, "selectBasketBeanById", HmBasketResultDTO.class);
        StringBuilder stringBuilder;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        for (HmBasketResultDTO hmBasketResultDTO : findBasketBeanById) {
            if (FinalDatas.ZERO == hmBasketResultDTO.getGid()) {
                hmBasketResultDTO.setFoodName("其他");
                hmBasketResultDTO.setAmount("1件");
                hmBasketResultDTO.setPrice(hmBasketResultDTO.getPrice() + "元");
            } else {
                CartGoodsBean cartGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(hmBasketResultDTO.getGid());
                hmBasketResultDTO.setFoodName(cartGoodsBean.getGname());
                stringBuilder = new StringBuilder();
                //数量
                String amount = hmBasketResultDTO.getAmount();
                //decimalFormat格式转换
                stringBuilder.append(decimalFormat.format(Double.parseDouble(amount))).append("斤");
                hmBasketResultDTO.setAmount(stringBuilder.toString());
                hmBasketResultDTO.setPrice(hmBasketResultDTO.getPrice() + "元");
            }
        }
        return ResultUtil.getSuccess(findBasketBeanById);
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

        return ResultUtil.getSuccess(null);
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
        this.dao.executeDeleteMethod(hmBasketDelQueryDTO, "deleteBeanById");

        return ResultUtil.getSuccess(null);
    }

    @Override
    public ResultDTO<Object> editInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO) {
        if (null == hmBasketEditQueryDTO) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        if (null == hmBasketEditQueryDTO.getId()) {
            return ResultUtil.getFail(CommonMessageEnum.PRI_ID_IS_EMPT);
        }
        CartBasketBean cartBasketBean = this.dao.selectByPrimaryKey(hmBasketEditQueryDTO.getId());
        //根据主键id查询得到gid 如果为0则为其他.根据价格修改. 如果不是0则根据数量修改
        if (FinalDatas.ZERO == cartBasketBean.getGid()) {
            if (StringUtils.isBlank(hmBasketEditQueryDTO.getPrice())) {
                return ResultUtil.getFail(CommonMessageEnum.PRICE_IS_NULL);
            } else {
                cartBasketBean.setPrice(new BigDecimal(hmBasketEditQueryDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                //获取时间用unix时间戳
                Long unixTime = System.currentTimeMillis() / 1000;
                cartBasketBean.setCtime(unixTime.intValue());
                if (StringUtil.isBlank(hmBasketEditQueryDTO.getRemark())) {
                    cartBasketBean.setRemark("");
                } else {
                    cartBasketBean.setRemark(hmBasketEditQueryDTO.getRemark());
                }
                this.dao.updateByPrimaryKey(cartBasketBean);
                return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
            }
        } else {
            if (StringUtils.isBlank(hmBasketEditQueryDTO.getAmount())) {
                return ResultUtil.getFail(CommonMessageEnum.AMOUNT_IS_NULL);
            }
        }
        //如果是页面上的减号判断如果传来的值为0则是删除操作.
        if (BigDecimal.ZERO.equals(new BigDecimal(hmBasketEditQueryDTO.getAmount()))) {
            HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
            hmBasketDelQueryDTO.setId(hmBasketEditQueryDTO.getId());
            hmBasketDelQueryDTO.setUid(cartBasketBean.getUid());
            ResultDTO<Object> dto = deleteInfoById(hmBasketDelQueryDTO);
            return dto;
        } else {
            //总价
            BigDecimal totalprice = cartBasketBean.getUnitprice().multiply(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
            cartBasketBean.setPrice(totalprice);
            cartBasketBean.setAmount(new BigDecimal(hmBasketEditQueryDTO.getAmount()));
            if (StringUtil.isBlank(hmBasketEditQueryDTO.getRemark())) {
                cartBasketBean.setRemark("");
            } else {
                cartBasketBean.setRemark(hmBasketEditQueryDTO.getRemark());
            }
            //获取时间用unix时间戳
            Long unixTime = System.currentTimeMillis() / 1000;
            cartBasketBean.setCtime(unixTime.intValue());
            this.dao.updateByPrimaryKeySelective(cartBasketBean);
        }
        return ResultUtil.getSuccess(null);
    }

    @Override
    public ResultDTO<Object> deleteAllByPid(HmBasketDelQueryDTO hmBasketDelQueryDTO) {
        if (null == hmBasketDelQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }
        if (null == hmBasketDelQueryDTO.getSids() || hmBasketDelQueryDTO.getSids().length < 0) {
            return ResultUtil.getFail(CommonMessageEnum.SID_IS_NULL);
        }
        this.dao.executeDeleteMethod(hmBasketDelQueryDTO, "deleteInfoBySids");

        return ResultUtil.getSuccess(null);
    }
}
