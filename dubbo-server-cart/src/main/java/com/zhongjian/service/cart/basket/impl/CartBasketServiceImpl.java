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
import com.zhongjian.dto.cart.basket.query.CartBasketDelQueryDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketEditQueryDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketListQueryDTO;
import com.zhongjian.dto.cart.basket.result.CartBasketResultDTO;
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
    public ResultDTO<Object> addOrUpdateInfo(CartBasketEditQueryDTO cartBasketEditQueryDTO) {
        //参数校验
        if (null == cartBasketEditQueryDTO) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketEditQueryDTO.getSid()) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketEditQueryDTO.getGid()) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketEditQueryDTO.getUid()) {
            return ResultUtil.getFail(null);
        }
        //当商品名称添加为其他的时候增加该字段
        if (FinalDatas.ZERO == cartBasketEditQueryDTO.getGid()) {
            if (StringUtils.isBlank(cartBasketEditQueryDTO.getPrice())) {
                return ResultUtil.getFail(null);
            }
        } else {
            if (null == cartBasketEditQueryDTO.getAmount() || BigDecimal.ZERO.equals(new BigDecimal(cartBasketEditQueryDTO.getAmount()))) {
                return ResultUtil.getFail(null);
            }
        }
        //根据前端传入的商品id去查询pid,
        CartGoodsBean cartGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(cartBasketEditQueryDTO.getGid());
        //根据gid,pid,uid查询购物车信息.
        CartParamDTO queryDTO = new CartParamDTO();
        queryDTO.setGid(cartBasketEditQueryDTO.getGid());
        queryDTO.setSid(cartBasketEditQueryDTO.getSid());
        queryDTO.setUid(cartBasketEditQueryDTO.getUid());
        CartBasketBean findBasketBeanById = this.dao.executeSelectOneMethod(queryDTO, "findBasketBeanById", CartBasketBean.class);
        //这边判断要是查询出来为空则为新增要是有数据则为更改
        CartBasketBean cartBasketBean = new CartBasketBean();
        BeanUtils.copyProperties(queryDTO, cartBasketBean);
        if (StringUtil.isBlank(cartBasketEditQueryDTO.getRemark())) {
            cartBasketBean.setRemark("");
        } else {
            cartBasketBean.setRemark(cartBasketEditQueryDTO.getRemark());
        }
        //获取时间用unix时间戳
        Long unixTime = System.currentTimeMillis() / 1000;
        cartBasketBean.setCtime(unixTime.intValue());
        if (null == findBasketBeanById) {
            if (FinalDatas.ZERO == cartBasketEditQueryDTO.getGid()) {
                cartBasketBean.setPrice(new BigDecimal(cartBasketEditQueryDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                cartBasketBean.setAmount(new BigDecimal(1));
                cartBasketBean.setUnitprice(cartBasketBean.getPrice());
                cartBasketBean.setRemark(cartBasketEditQueryDTO.getRemark());
            } else {
                cartBasketBean.setAmount(new BigDecimal(cartBasketEditQueryDTO.getAmount()));
                cartBasketBean.setUnitprice(cartGoodsBean.getPrice());
                //计算总价保留两个小数点
                //总价
                BigDecimal multiply = cartGoodsBean.getPrice().multiply(new BigDecimal(cartBasketEditQueryDTO.getAmount()));
                cartBasketBean.setPrice(multiply.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            this.dao.insertSelective(cartBasketBean);
        } else {
            if (FinalDatas.ZERO == cartBasketEditQueryDTO.getGid()) {
                cartBasketBean.setId(findBasketBeanById.getId() );
                cartBasketBean.setCtime(unixTime.intValue());
                BigDecimal add = findBasketBeanById.getPrice().add(new BigDecimal(cartBasketEditQueryDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                cartBasketBean.setPrice(add);
                cartBasketBean.setRemark(cartBasketEditQueryDTO.getRemark());
                cartBasketBean.setUnitprice(cartBasketBean.getPrice());
            }else{
                cartBasketBean.setId(findBasketBeanById.getId());
                //计算新的总价(去good里面获取价格重新计算)
                BigDecimal add = findBasketBeanById.getAmount().add(new BigDecimal(cartBasketEditQueryDTO.getAmount()));
                BigDecimal multiply1 = add.multiply(cartGoodsBean.getPrice());
                cartBasketBean.setPrice(multiply1.setScale(2, BigDecimal.ROUND_HALF_UP));
                cartBasketBean.setAmount(findBasketBeanById.getAmount().add(new BigDecimal(cartBasketEditQueryDTO.getAmount())));
                cartBasketBean.setUnitprice(cartGoodsBean.getPrice());
            }
            this.dao.updateByPrimaryKeySelective(cartBasketBean);

        }
        return ResultUtil.getSuccess(null);
    }

    @Override
    public ResultDTO<Object> queryList(CartBasketListQueryDTO cartBasketListQueryDTO) {
        if (null == cartBasketListQueryDTO) {
            return ResultUtil.getFail(null);
        }

        if (null == cartBasketListQueryDTO.getUid()) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketListQueryDTO.getSid()) {
            return ResultUtil.getFail(null);
        }
        //根据用户id和商户id查找basket表中的数据.
        CartParamDTO hmBasketParamDTO = new CartParamDTO();
        hmBasketParamDTO.setSid(cartBasketListQueryDTO.getSid());
        hmBasketParamDTO.setUid(cartBasketListQueryDTO.getUid());
        List<CartBasketResultDTO> findBasketBeanById = this.dao.executeListMethod(hmBasketParamDTO, "selectBasketBeanById", CartBasketResultDTO.class);
        StringBuilder stringBuilder;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        for (CartBasketResultDTO cartBasketResultDTO : findBasketBeanById) {
            if (FinalDatas.ZERO == cartBasketResultDTO.getGid()) {
                cartBasketResultDTO.setFoodName("其他");
                cartBasketResultDTO.setAmount("1件");
                cartBasketResultDTO.setPrice(cartBasketResultDTO.getPrice() + "元");
            } else {
                CartGoodsBean cartGoodsBean = this.hmGoodsBeanDAO.selectByPrimaryKey(cartBasketResultDTO.getGid());
                cartBasketResultDTO.setFoodName(cartGoodsBean.getGname());
                stringBuilder = new StringBuilder();
                //数量
                String amount = cartBasketResultDTO.getAmount();
                //decimalFormat格式转换
                stringBuilder.append(decimalFormat.format(Double.parseDouble(amount))).append(cartGoodsBean.getUnit());
                cartBasketResultDTO.setAmount(stringBuilder.toString());
                cartBasketResultDTO.setPrice(cartBasketResultDTO.getPrice() + "元");
            }
        }
        return ResultUtil.getSuccess(findBasketBeanById);
    }

    @Override
    public ResultDTO<Object> deleteInfoById(CartBasketDelQueryDTO cartBasketDelQueryDTO) {

        if (null == cartBasketDelQueryDTO) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketDelQueryDTO.getId()) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketDelQueryDTO.getUid()) {
            return ResultUtil.getFail(null);
        }
        this.dao.executeDeleteMethod(cartBasketDelQueryDTO, "deleteBeanById");

        return ResultUtil.getSuccess(null);
    }

    @Override
    public ResultDTO<Object> deleteAllInfoById(CartBasketDelQueryDTO cartBasketDelQueryDTO) {
        if (null == cartBasketDelQueryDTO) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketDelQueryDTO.getSid()) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketDelQueryDTO.getUid()) {
            return ResultUtil.getFail(null);
        }
        this.dao.executeDeleteMethod(cartBasketDelQueryDTO, "deleteBeanById");

        return ResultUtil.getSuccess(null);
    }

    @Override
    public ResultDTO<Object> editInfo(CartBasketEditQueryDTO cartBasketEditQueryDTO) {
        if (null == cartBasketEditQueryDTO) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketEditQueryDTO.getId()) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketEditQueryDTO.getUid()) {
            return ResultUtil.getFail(null);
        }
        CartParamDTO cartParamDTO = new CartParamDTO();
        cartParamDTO.setId(cartBasketEditQueryDTO.getId());
        cartParamDTO.setUid(cartBasketEditQueryDTO.getUid());
        CartBasketBean cartBasketBean = this.dao.executeSelectOneMethod(cartParamDTO,"selectBasketInfoById",CartBasketBean.class);
        //根据主键id查询得到gid 如果为0则为其他.根据价格修改. 如果不是0则根据数量修改
        if (FinalDatas.ZERO == cartBasketBean.getGid()) {
            if (StringUtils.isBlank(cartBasketEditQueryDTO.getPrice())) {
                return ResultUtil.getFail(null);
            } else {
                BigDecimal bigDecimal = new BigDecimal(cartBasketEditQueryDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                cartBasketBean.setPrice(bigDecimal);
                //获取时间用unix时间戳
                Long unixTime = System.currentTimeMillis() / 1000;
                cartBasketBean.setCtime(unixTime.intValue());
                cartBasketBean.setUnitprice(bigDecimal);
                if (StringUtil.isBlank(cartBasketEditQueryDTO.getRemark())) {
                    cartBasketBean.setRemark("");
                } else {
                    cartBasketBean.setRemark(cartBasketEditQueryDTO.getRemark());
                }
                this.dao.updateByPrimaryKey(cartBasketBean);
                return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
            }
        } else {
            if (StringUtils.isBlank(cartBasketEditQueryDTO.getAmount())) {
                return ResultUtil.getFail(null);
            }
        }
        //如果是页面上的减号判断如果传来的值为0则是删除操作.
        if (BigDecimal.ZERO.equals(new BigDecimal(cartBasketEditQueryDTO.getAmount()))) {
            CartBasketDelQueryDTO cartBasketDelQueryDTO = new CartBasketDelQueryDTO();
            cartBasketDelQueryDTO.setId(cartBasketEditQueryDTO.getId());
            cartBasketDelQueryDTO.setUid(cartBasketBean.getUid());
            ResultDTO<Object> dto = deleteInfoById(cartBasketDelQueryDTO);
            return dto;
        } else {
            //总价
            BigDecimal totalprice = cartBasketBean.getUnitprice().multiply(new BigDecimal(cartBasketEditQueryDTO.getAmount()));
            cartBasketBean.setPrice(totalprice);
            cartBasketBean.setAmount(new BigDecimal(cartBasketEditQueryDTO.getAmount()));
            if (StringUtil.isBlank(cartBasketEditQueryDTO.getRemark())) {
                cartBasketBean.setRemark("");
            } else {
                cartBasketBean.setRemark(cartBasketEditQueryDTO.getRemark());
            }
            //获取时间用unix时间戳
            Long unixTime = System.currentTimeMillis() / 1000;
            cartBasketBean.setCtime(unixTime.intValue());
            this.dao.updateByPrimaryKeySelective(cartBasketBean);
        }
        return ResultUtil.getSuccess(null);
    }

    @Override
    public ResultDTO<Object> deleteAllByPid(CartBasketDelQueryDTO cartBasketDelQueryDTO) {
        if (null == cartBasketDelQueryDTO.getUid()) {
            return ResultUtil.getFail(null);
        }
        if (null == cartBasketDelQueryDTO.getSids() || cartBasketDelQueryDTO.getSids().length < 0) {
            return ResultUtil.getFail(null);
        }
        this.dao.executeDeleteMethod(cartBasketDelQueryDTO, "deleteInfoBySids");

        return ResultUtil.getSuccess(null);
    }
}
