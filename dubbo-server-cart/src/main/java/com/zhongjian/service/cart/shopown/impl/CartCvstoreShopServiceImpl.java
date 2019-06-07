package com.zhongjian.service.cart.shopown.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.cart.cvstore.CartCvstoreBean;
import com.zhongjian.dao.entity.cart.goods.CartGoodsBean;
import com.zhongjian.dao.entity.cart.shopown.CartShopownBean;
import com.zhongjian.dao.entity.cart.store.CartStoreStpBean;
import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.cart.basket.query.CartBasketListQueryDTO;
import com.zhongjian.dto.cart.basket.result.CartBasketResultDTO;
import com.zhongjian.dto.cart.cvstore.result.CvstoreList;
import com.zhongjian.dto.cart.market.result.*;
import com.zhongjian.dto.cart.shopown.result.CartShopownResultDTO;
import com.zhongjian.dto.cart.storeActivity.result.CartStoreActivityResultDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.service.cart.shopown.CartCvStoreShopService;
import com.zhongjian.util.LogUtil;
import com.zhongjian.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ldd
 */
@Service("cartCvStoreShopService")
public class CartCvstoreShopServiceImpl extends HmBaseService<CartShopownBean, Integer> implements CartCvStoreShopService {


    private HmDAO<UserBean, Integer> hmDAO;

    private HmDAO<CartCvstoreBean, Integer> hmCvstoreBeanDAO;

    private HmDAO<CartStoreStpBean, Integer> cartStoreStpBean;

    private HmDAO<CartGoodsBean, Integer> cartGoodsBeanHmDAO;

    @Resource
    private void setHmStoreActivityHmDAO(HmDAO<CartStoreStpBean, Integer> cartStoreStpBean) {
        this.cartStoreStpBean = cartStoreStpBean;
        this.cartStoreStpBean.setPerfix(CartStoreStpBean.class.getName());
    }

    @Resource
    private void setHmBasketBeanDAO(HmDAO<CartCvstoreBean, Integer> hmCvstoreBeanDAO) {
        this.hmCvstoreBeanDAO = hmCvstoreBeanDAO;
        this.hmCvstoreBeanDAO.setPerfix(CartCvstoreBean.class.getName());
    }

    @Resource
    private void setHmDAO(HmDAO<UserBean, Integer> hmDAO) {
        this.hmDAO = hmDAO;
        this.hmDAO.setPerfix(UserBean.class.getName());
    }


    @Resource
    private void setCartGoodsBeanHmDAO(HmDAO<CartGoodsBean, Integer> cartGoodsBeanHmDAO) {
        this.cartGoodsBeanHmDAO = cartGoodsBeanHmDAO;
        this.cartGoodsBeanHmDAO.setPerfix(CartGoodsBean.class.getName());
    }

    @Override
    public ResultDTO<Object> queryList(Integer uid) {
        ResultDTO<CartMarketResultListDTO> resultDTO = new ResultDTO<CartMarketResultListDTO>();
        if (null == uid) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        CvstoreList cvstoreList = new CvstoreList();
        StringBuilder stringbuider;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        //将所有开张的价格相加.
        BigDecimal numberByOpen = null;
        //将所有打烊的价格相加
        BigDecimal numberByClose = null;
        //便利店打烊
        List<CartShopownResultDTO> cvStoreByclose = new ArrayList<>();
        //便利店开张
        List<CartShopownResultDTO> cvStoreByOpen = new ArrayList<>();
        //将所有开张的优惠价格相加.
        BigDecimal priceByOpen = BigDecimal.ZERO;
        //将所有打烊的优惠价格相加
        BigDecimal priceByClose = BigDecimal.ZERO;
        //起步价打烊
        BigDecimal useForStartPriceByClose = null;
        //起步价开张
        BigDecimal useForStartPriceByOpen = null;

        List<CartShopownResultDTO> findShopownInfoByUid = this.dao.executeListMethod(uid, "findShopownInfoByUid", CartShopownResultDTO.class);
        for (CartShopownResultDTO shopownResultDTO : findShopownInfoByUid) {
            shopownResultDTO.setPicture("/upload/" + shopownResultDTO.getPicture());

            if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                shopownResultDTO.setStatusMsg("开张");
            }
            if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                shopownResultDTO.setStatusMsg("打烊");
            }
            if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                shopownResultDTO.setStatusMsg("预约中");
            }

            //这个商户下用户购买的食品信息
            CartBasketListQueryDTO cartBasketListQueryDTO = new CartBasketListQueryDTO();
            cartBasketListQueryDTO.setUid(uid);
            cartBasketListQueryDTO.setSid(shopownResultDTO.getPid());
            List<CartBasketResultDTO> findBasketById = this.hmCvstoreBeanDAO.executeListMethod(cartBasketListQueryDTO, "findCvStoreById", CartBasketResultDTO.class);
            List<String> remarkList = new ArrayList<String>();
            //将所有开张的总价相加.
            List<BigDecimal> listByOpen = new ArrayList<>();
            //将所有打烊的总价相加
            List<BigDecimal> listByClose = new ArrayList<>();
            for (CartBasketResultDTO cartBasketResultDTO : findBasketById) {

                //根据食品id得到食品信息
                CartGoodsBean cartGoodsBean = this.cartGoodsBeanHmDAO.selectByPrimaryKey(cartBasketResultDTO.getGid());
                if (null == cartGoodsBean) {
                    LogUtil.info("根据食品id得不到食品信息", "cartGoodsBean:" + cartGoodsBean);
                } else {
                    stringbuider = new StringBuilder();
                    stringbuider.append(cartBasketResultDTO.getUnitPrice()).append("元/").append(cartGoodsBean.getUnit());
                    cartBasketResultDTO.setUnitPrice(stringbuider.toString());
                    cartBasketResultDTO.setUnit(cartGoodsBean.getUnit());

                    if (FinalDatas.ZERO == cartBasketResultDTO.getGid()) {
                        cartBasketResultDTO.setFoodName("其他");
                        cartBasketResultDTO.setStatus(1);
                    } else {
                        cartBasketResultDTO.setFoodName(cartGoodsBean.getGname());
                    }
                }
                cartBasketResultDTO.setAmount(decimalFormat.format(Double.parseDouble(cartBasketResultDTO.getAmount())));
                cartBasketResultDTO.setTotalPrice("¥" + decimalFormat.format(Double.parseDouble(cartBasketResultDTO.getTotalPrice())));

                //判断要是没有remark备注就不需要拼接组装;
                if (!StringUtils.isBlank(cartBasketResultDTO.getRemark())) {
                    //拼接备注信息:[名字]+备注信息
                    String remart = ("[") + (cartBasketResultDTO.getFoodName()) + ("]") + (cartBasketResultDTO.getRemark());
                    remarkList.add(remart);
                }
                //将用户在商品下的所有总价存入list里面.
                if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                    listByClose.add(new BigDecimal(cartBasketResultDTO.getTotalPrice().substring(1)));
                } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    listByOpen.add(new BigDecimal(cartBasketResultDTO.getTotalPrice().substring(1)));
                }
            }
            //备注信息
            shopownResultDTO.setRemarkList(remarkList);
            //该用户在商家下对应的食品信息
            shopownResultDTO.setBasket(findBasketById);
            if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                numberByClose = new BigDecimal(0);
                for (int i = 0; i < listByClose.size(); i++) {
                    numberByClose = listByClose.get(i).add(numberByClose);
                }
            } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                numberByOpen = new BigDecimal(0);
                for (int i = 0; i < listByOpen.size(); i++) {
                    numberByOpen = listByOpen.get(i).add(numberByOpen);
                }

            }
            //拼接每个商户下的活动信息
            List<CartStoreActivityResultDTO> cartStoreActivityResultDTOS = shopownResultDTO.getStoreActivity();
            if (CollectionUtils.isEmpty(cartStoreActivityResultDTOS)) {
                LogUtil.info("该商家没有优惠活动", "cartStoreActivityResultDTOS" + cartStoreActivityResultDTOS);
            } else {
                stringbuider = new StringBuilder();
                for (int i = cartStoreActivityResultDTOS.size() - 1; i >= 0; i--) {
                    CartStoreActivityResultDTO cartStoreActivityResultDTO = cartStoreActivityResultDTOS.get(i);
                    if (FinalDatas.ZERO == cartStoreActivityResultDTO.getType()) {
                        stringbuider.append(decimalFormat.format(Double.parseDouble(cartStoreActivityResultDTO.getFull()))).append("减").append(
                                decimalFormat.format(Double.parseDouble(cartStoreActivityResultDTO.getReduce()))
                        ).append(",");
                    }
                    if (FinalDatas.ONE == cartStoreActivityResultDTO.getType()) {
                        stringbuider.append(decimalFormat.format(new BigDecimal(cartStoreActivityResultDTO.getDiscount()).multiply(BigDecimal.TEN))).append("折");
                    }
                }
                //去除末尾逗号
                String buider = stringbuider.toString();
                if (!StringUtil.isBlank(buider) && (",".equals(stringbuider.toString().substring(buider.length() - 1, buider.length())))) {
                    String activityMsg = stringbuider.toString().substring(0, stringbuider.toString().length() - 1);
                    shopownResultDTO.setActivityMsg(activityMsg);
                } else {
                    shopownResultDTO.setActivityMsg(buider);
                }
                for (CartStoreActivityResultDTO cartStoreActivityResultDTO : cartStoreActivityResultDTOS) {
                    //折扣的优惠
                    if (FinalDatas.ONE == cartStoreActivityResultDTO.getType()) {
                        if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                            priceByClose = numberByClose.multiply(new BigDecimal(cartStoreActivityResultDTO.getDiscount()));
                        } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                            priceByOpen = numberByOpen.multiply(new BigDecimal(cartStoreActivityResultDTO.getDiscount()));
                        }
                    }
                    //满减的优惠
                    if (FinalDatas.ZERO == cartStoreActivityResultDTO.getType()) {
                        //根据查询数据库倒序排列判断如果大于1则总价大于满减价那么就减去优惠值
                        //优惠价
                        BigDecimal reduce = new BigDecimal(cartStoreActivityResultDTO.getReduce());
                        if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                            if (numberByClose.compareTo(new BigDecimal(cartStoreActivityResultDTO.getFull())) >= 0) {
                                priceByClose = numberByClose.subtract(reduce);
                                break;
                            }
                        } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                            if (numberByOpen.compareTo(new BigDecimal(cartStoreActivityResultDTO.getFull())) >= 0) {
                                priceByOpen = numberByOpen.subtract(reduce);
                                break;
                            }
                        }
                    }
                }
                //商户下的总优惠价
                if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                    if (BigDecimal.ZERO.compareTo(priceByClose) != 0) {
                        shopownResultDTO.setDiscountPrice(String.valueOf(priceByClose.setScale(2,BigDecimal.ROUND_HALF_UP)));
                        useForStartPriceByClose = priceByClose;
                    }
                } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    if (BigDecimal.ZERO.compareTo(priceByOpen) != 0) {
                        shopownResultDTO.setDiscountPrice(String.valueOf(priceByOpen.setScale(2,BigDecimal.ROUND_HALF_UP)));
                        useForStartPriceByOpen = priceByOpen;
                    }
                }
            }
            //商户下的总价
            if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                shopownResultDTO.setTotalPrice(String.valueOf(numberByClose.setScale(2,BigDecimal.ROUND_HALF_UP)));
                if (null == useForStartPriceByClose) {
                    useForStartPriceByClose = numberByClose;
                }
            } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                shopownResultDTO.setTotalPrice(String.valueOf(numberByOpen.setScale(2,BigDecimal.ROUND_HALF_UP)));
                if (null == useForStartPriceByOpen) {
                    useForStartPriceByOpen = numberByOpen;
                }
            }

            //计算起步价.
            //这边计算便利店起步价
            CartStoreStpBean findCvStoreActivityByPid = this.cartStoreStpBean.executeSelectOneMethod(cartBasketListQueryDTO.getSid(), "findCvStoreActivityByPid", CartStoreStpBean.class);

            if (null == findCvStoreActivityByPid) {
                LogUtil.info("该商家没有起步价", "findCvStoreActivityByPid" + findCvStoreActivityByPid);
            }else{
                shopownResultDTO.setStartingPrice(String.valueOf(findCvStoreActivityByPid.getStartingPrice()));

                //起步价判断
                if (useForStartPriceByClose != null) {
                    if (useForStartPriceByClose.compareTo(findCvStoreActivityByPid.getStartingPrice()) >= 0) {
                        shopownResultDTO.setState(1);
                    } else {
                        //差价
                        String disparity = String.valueOf(findCvStoreActivityByPid.getStartingPrice().subtract(useForStartPriceByClose));
                        shopownResultDTO.setDisparity(disparity);
                        shopownResultDTO.setState(0);
                    }
                }
                //起步价判断
                if (useForStartPriceByOpen != null) {
                    if (useForStartPriceByOpen.compareTo(findCvStoreActivityByPid.getStartingPrice()) >= 0) {
                        shopownResultDTO.setState(1);
                    } else {
                        //差价
                        String disparity = String.valueOf(findCvStoreActivityByPid.getStartingPrice().subtract(useForStartPriceByOpen));
                        shopownResultDTO.setDisparity(disparity);
                        shopownResultDTO.setState(0);
                    }
                }
            }
            if (shopownResultDTO.getStatus().equals(FinalDatas.ONE.toString())) {
                cvStoreByclose.add(shopownResultDTO);
            } else {
                cvStoreByOpen.add(shopownResultDTO);
            }
        }
        if (!CollectionUtils.isEmpty(cvStoreByclose)) {
            cvstoreList.setClose(cvStoreByclose);
        }
        if (!CollectionUtils.isEmpty(cvStoreByOpen)) {
            cvstoreList.setOpen(cvStoreByOpen);
        }

        return ResultUtil.getSuccess(cvstoreList);
    }
}




