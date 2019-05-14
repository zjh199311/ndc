package com.zhongjian.service.cart.shopown.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.cart.CartParamDTO;
import com.zhongjian.common.constant.CountDTO;
import com.zhongjian.dao.entity.cart.basket.CartBasketBean;
import com.zhongjian.dao.entity.cart.goods.CartGoodsBean;
import com.zhongjian.dao.entity.cart.market.CartMarketBean;
import com.zhongjian.dao.entity.cart.rider.CartRiderOrderBean;
import com.zhongjian.dao.entity.cart.store.CartStoreActivityBean;
import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.cart.marketActivity.result.CartMarketActivityResultDTO;
import com.zhongjian.dto.cart.storeActivity.result.CartStoreActivityResultDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.cart.basket.query.CartBasketListQueryDTO;
import com.zhongjian.dto.cart.basket.result.CartBasketResultDTO;
import com.zhongjian.dto.cart.market.result.*;
import com.zhongjian.dto.cart.shopown.result.CartShopownResultDTO;
import com.zhongjian.service.cart.shopown.CartShopownService;
import com.zhongjian.util.DateUtil;
import com.zhongjian.util.LogUtil;
import com.zhongjian.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: ldd
 */
@Service("hmShopownService")
public class CartShopownServiceImpl extends HmBaseService<CartMarketBean, Integer> implements CartShopownService {

    private HmDAO<UserBean, Integer> hmDAO;

    private HmDAO<CartBasketBean, Integer> hmBasketBeanDAO;

    private HmDAO<CartStoreActivityBean, Integer> hmStoreActivityHmDAO;

    private HmDAO<CartRiderOrderBean, Integer> cartRiderOrderDAO;

    private HmDAO<CartGoodsBean, Integer> cartGoodsBeanHmDAO;

    @Resource
    private void setHmStoreActivityHmDAO(HmDAO<CartStoreActivityBean, Integer> hmStoreActivityHmDAO) {
        this.hmStoreActivityHmDAO = hmStoreActivityHmDAO;
        this.hmStoreActivityHmDAO.setPerfix(CartStoreActivityBean.class.getName());
    }

    @Resource
    private void setHmBasketBeanDAO(HmDAO<CartBasketBean, Integer> hmBasketBeanDAO) {
        this.hmBasketBeanDAO = hmBasketBeanDAO;
        this.hmBasketBeanDAO.setPerfix(CartBasketBean.class.getName());
    }

    @Resource
    private void setHmDAO(HmDAO<UserBean, Integer> hmDAO) {
        this.hmDAO = hmDAO;
        this.hmDAO.setPerfix(UserBean.class.getName());
    }

    @Resource
    private void setCartRiderOrderDAO(HmDAO<CartRiderOrderBean, Integer> cartRiderOrderDAO) {
        this.cartRiderOrderDAO = cartRiderOrderDAO;
        this.cartRiderOrderDAO.setPerfix(CartRiderOrderBean.class.getName());
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
        //组装打烊预约开张DTO
        CartMarketResultListDTO cartMarketResultListDTO = new CartMarketResultListDTO();
        //预约
        List<CartMarketResultByAdvenceDTO> cartMarketResultByAdvenceDTOList = new ArrayList<>();
        //打烊
        List<CartMarketResultByCloseDTO> cartMarketResultByCloseDTOList = new ArrayList<>();
        //开张
        List<CartMarketResultByOpenDTO> cartMarketResultByOpenDTOList = new ArrayList<>();

        StringBuilder stringbuider;

        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        //当前时间减去今天的最小时间并根据uid查询并且状态为待支付和支付成功时.该用户就不为首单了.那么该用户则不能被菜场优惠
        CartParamDTO cartParamDTO = new CartParamDTO();
        Long todayZeroTime = DateUtil.getTodayZeroTime();
        cartParamDTO.setCtime(todayZeroTime.intValue());
        cartParamDTO.setUid(uid);
        Integer findCountByUid = this.cartRiderOrderDAO.executeSelectOneMethod(cartParamDTO, "findCountByUid", Integer.class);

        //获取菜场信息
        List<CartMarketResultDTO> findMarketByUid = this.dao.executeListMethod(uid, "findMarketByUid", CartMarketResultDTO.class);
        for (CartMarketResultDTO hmShopownResultDTO : findMarketByUid) {
            //开张
            CartMarketResultByOpenDTO cartMarketResultByOpenDTO = new CartMarketResultByOpenDTO();
            //预约
            CartMarketResultByAdvenceDTO cartMarketResultByAdvenceDTO = new CartMarketResultByAdvenceDTO();
            //打烊
            CartMarketResultByCloseDTO cartMarketResultByCloseDTO = new CartMarketResultByCloseDTO();
            //商家信息(开张)
            List<CartShopownResultDTO> cartShopownResultDTOListByOpen = new ArrayList<>();
            //商家信息(预约)
            List<CartShopownResultDTO> cartShopownResultDTOListByAdvence = new ArrayList<>();
            //商家信息(打烊)
            List<CartShopownResultDTO> cartShopownResultDTOListByClose = new ArrayList<>();
            //得到商家信息
            List<CartShopownResultDTO> cartShopownResultDTOS = hmShopownResultDTO.getShopown();
            //总价优惠后价格(开张)
            BigDecimal totalDisPriceByOpen = BigDecimal.ZERO;
            //总价优惠后价格(预约)
            BigDecimal totalDisPriceByAdvence = BigDecimal.ZERO;
            //总价优惠后价格(打烊)
            BigDecimal totalDisPriceByClose = BigDecimal.ZERO;
            //将所有开张的价格相加.
            BigDecimal numberByOpen = null;
            //将所有预约的价格相加
            BigDecimal numberByAdvence = null;
            //将所有打烊的价格相加
            BigDecimal numberByClose = null;
            //开张总价是否满足菜场价格
            BigDecimal marketByOpen = BigDecimal.ZERO;
            //预约总价是否满足菜场价格
            BigDecimal marketByAdvence = BigDecimal.ZERO;
            //打烊总价是否满足菜场价格
            BigDecimal marketByClose = BigDecimal.ZERO;

            for (CartShopownResultDTO shopownResultDTO : cartShopownResultDTOS) {
                shopownResultDTO.setPicture("/" + shopownResultDTO.getPicture());
                //将所有开张的优惠价格相加.
                BigDecimal priceByOpen = BigDecimal.ZERO;
                //将所有预约的优惠价格相加
                BigDecimal priceByAdvence = BigDecimal.ZERO;
                //将所有打烊的优惠价格相加
                BigDecimal priceByClose = BigDecimal.ZERO;
                if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setStatusMsg("开张");
                }
                if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setStatusMsg("打烊");
                }
                if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setStatusMsg("预约中");
                }
                //拼接每个商户下的活动信息
                List<CartStoreActivityResultDTO> cartStoreActivityResultDTOS = shopownResultDTO.getStoreActivity();
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
                } else if (!StringUtil.isBlank(buider)) {
                    shopownResultDTO.setActivityMsg(buider.toString());
                }
                //这个商户下用户购买的食品信息
                CartBasketListQueryDTO cartBasketListQueryDTO = new CartBasketListQueryDTO();
                cartBasketListQueryDTO.setUid(uid);
                cartBasketListQueryDTO.setSid(shopownResultDTO.getPid());
                List<CartBasketResultDTO> findBasketById = this.hmBasketBeanDAO.executeListMethod(cartBasketListQueryDTO, "findBasketById", CartBasketResultDTO.class);
                List<String> remarkList = new ArrayList<String>();
                //将所有开张的总价相加.
                List<BigDecimal> listByOpen = new ArrayList<>();
                //将所有预约的总价相加.
                List<BigDecimal> listByAdvence = new ArrayList<>();
                //将所有打烊的总价相加.
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
                    }
                    cartBasketResultDTO.setAmount(decimalFormat.format(Double.parseDouble(cartBasketResultDTO.getAmount())));
                    cartBasketResultDTO.setTotalPrice("¥" + decimalFormat.format(Double.parseDouble(cartBasketResultDTO.getTotalPrice())));

                    if (FinalDatas.ZERO == cartBasketResultDTO.getGid()) {
                        cartBasketResultDTO.setFoodName("其他");
                        cartBasketResultDTO.setStatus(1);
                    } else {
                        cartBasketResultDTO.setFoodName(cartGoodsBean.getGname());
                    }
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
                    } else if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                        listByAdvence.add(new BigDecimal(cartBasketResultDTO.getTotalPrice().substring(1)));
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
                } else if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    numberByAdvence = new BigDecimal(0);
                    for (int i = 0; i < listByAdvence.size(); i++) {
                        numberByAdvence = listByAdvence.get(i).add(numberByAdvence);
                    }
                }
                /**
                 *   这边逻辑. 因为商品的总价是放在商户的bean中. 而活动优惠价格的bean放在商户bean中的.  所有要先计算完总价才去判断该商户是否有活动.因为该活动表中的
                 *   full字段.在这边取不到.写在上面的活动循环中就得不到总值.这边就根据pid去查询数据库
                 */
                List<CartStoreActivityResultDTO> findStoreActivityBySid = this.hmStoreActivityHmDAO.executeListMethod(shopownResultDTO.getPid(), "findStoreActivityBySid", CartStoreActivityResultDTO.class);
                if (CollectionUtils.isEmpty(findStoreActivityBySid)) {
                    LogUtil.info("该商家没有优惠活动", "findStoreActivityBySid" + findStoreActivityBySid);
                } else {
                    for (CartStoreActivityResultDTO cartStoreActivityResultDTO : findStoreActivityBySid) {
                        //折扣的优惠
                        if (FinalDatas.ONE == cartStoreActivityResultDTO.getType()) {
                            if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                                priceByClose = numberByClose.multiply(new BigDecimal(cartStoreActivityResultDTO.getDiscount()));
                            } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                                priceByOpen = numberByOpen.multiply(new BigDecimal(cartStoreActivityResultDTO.getDiscount()));
                            } else {
                                priceByAdvence = numberByAdvence.multiply(new BigDecimal(cartStoreActivityResultDTO.getDiscount()));
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
                            } else {
                                if (numberByAdvence.compareTo(new BigDecimal(cartStoreActivityResultDTO.getFull())) >= 0) {
                                    priceByAdvence = numberByAdvence.subtract(reduce);
                                    break;
                                }
                            }

                        }
                    }
                    //商户下的总优惠价
                    if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                        if (BigDecimal.ZERO.compareTo(priceByClose) != 0) {
                            shopownResultDTO.setDiscountPrice(String.valueOf(priceByClose.setScale(2)));
                        }
                    } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                        if (BigDecimal.ZERO.compareTo(priceByOpen) != 0) {
                            shopownResultDTO.setDiscountPrice(String.valueOf(priceByOpen.setScale(2)));
                        }
                    } else if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                        if (BigDecimal.ZERO.compareTo(priceByAdvence) != 0) {
                            shopownResultDTO.setDiscountPrice(String.valueOf(priceByAdvence.setScale(2)));
                        }
                    }
                }
                //商户下的总价
                if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setTotalPrice(String.valueOf(numberByClose.setScale(2)));
                } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setTotalPrice(String.valueOf(numberByOpen.setScale(2)));
                } else if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setTotalPrice(String.valueOf(numberByAdvence.setScale(2)));
                }
                //这边封装DTO. 将开张预约打烊封装在对应的DTO里
                if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    cartMarketResultByAdvenceDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    cartMarketResultByAdvenceDTO.setType(2);
                    cartMarketResultByAdvenceDTO.setRule(hmShopownResultDTO.getRule());
                    cartMarketResultByAdvenceDTO.setStatus(hmShopownResultDTO.getStatus());
                    cartMarketResultByAdvenceDTO.setMarketActivity(hmShopownResultDTO.getMarketActivity());

                    //组装菜场DTO 如果一个菜场对应各自的商户.如果没有值则添加如果有值则判断该商户是否为该菜场旗下.
                    if (null == cartMarketResultByAdvenceDTO.getMartketId()) {
                        cartMarketResultByAdvenceDTO.setMartketId(hmShopownResultDTO.getMarketId());
                        cartShopownResultDTOListByAdvence.add(shopownResultDTO);
                    } else {
                        if (hmShopownResultDTO.getMarketId() == cartMarketResultByAdvenceDTO.getMartketId()) {
                            cartShopownResultDTOListByAdvence.add(shopownResultDTO);
                        } else {
                            cartShopownResultDTOListByAdvence = new ArrayList<>();
                            cartShopownResultDTOListByAdvence.add(shopownResultDTO);
                        }
                    }
                    if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getUnFavorable())) {
                        if (BigDecimal.ZERO.compareTo(priceByAdvence) < 0) {
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                        } else {
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                        }
                    } else {
                        marketByAdvence = marketByAdvence.add(numberByAdvence);
                    }
                    cartMarketResultByAdvenceDTO.setShopown(cartShopownResultDTOListByAdvence);

                } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    cartMarketResultByOpenDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    cartMarketResultByOpenDTO.setRule(hmShopownResultDTO.getRule());
                    cartMarketResultByOpenDTO.setStatus(hmShopownResultDTO.getStatus());
                    cartMarketResultByOpenDTO.setType(0);
                    cartMarketResultByOpenDTO.setMarketActivity(hmShopownResultDTO.getMarketActivity());

                    //组装菜场DTO 如果一个菜场对应各自的商户.如果没有值则添加如果有值则判断该商户是否为该菜场旗下.
                    if (null == cartMarketResultByOpenDTO.getMartketId()) {
                        cartMarketResultByOpenDTO.setMartketId(hmShopownResultDTO.getMarketId());
                        cartShopownResultDTOListByOpen.add(shopownResultDTO);
                    } else {
                        if (hmShopownResultDTO.getMarketId() == cartMarketResultByOpenDTO.getMartketId()) {
                            cartShopownResultDTOListByOpen.add(shopownResultDTO);
                        } else {
                            cartShopownResultDTOListByOpen = new ArrayList<>();
                            cartShopownResultDTOListByOpen.add(shopownResultDTO);
                        }
                    }
                    if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getUnFavorable())) {
                        if (BigDecimal.ZERO.compareTo(priceByOpen) < 0) {
                            totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                        } else {
                            totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                        }
                    } else {
                        marketByOpen = marketByOpen.add(numberByOpen);
                    }
                    cartMarketResultByOpenDTO.setShopown(cartShopownResultDTOListByOpen);
                } else {
                    cartMarketResultByCloseDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    cartMarketResultByCloseDTO.setRule(hmShopownResultDTO.getRule());
                    cartMarketResultByCloseDTO.setStatus(hmShopownResultDTO.getStatus());
                    cartMarketResultByCloseDTO.setType(1);
                    cartMarketResultByCloseDTO.setMarketActivity(hmShopownResultDTO.getMarketActivity());

                    //组装菜场DTO 如果一个菜场对应各自的商户.如果没有值则添加如果有值则判断该商户是否为该菜场旗下.
                    if (null == cartMarketResultByCloseDTO.getMartketId()) {
                        cartMarketResultByCloseDTO.setMartketId(hmShopownResultDTO.getMarketId());
                        cartShopownResultDTOListByClose.add(shopownResultDTO);
                    } else {
                        if (hmShopownResultDTO.getMarketId() == cartMarketResultByCloseDTO.getMartketId()) {
                            cartShopownResultDTOListByClose.add(shopownResultDTO);
                        } else {
                            cartShopownResultDTOListByClose = new ArrayList<>();
                            cartShopownResultDTOListByClose.add(shopownResultDTO);
                        }
                    }
                    if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getUnFavorable())) {
                        if (BigDecimal.ZERO.compareTo(priceByClose) < 0) {
                            totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                        } else {
                            totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                        }
                    } else {
                        marketByClose = marketByClose.add(numberByClose);
                    }

                    cartMarketResultByCloseDTO.setShopown(cartShopownResultDTOListByClose);
                }
            }

            if (null != cartMarketResultByAdvenceDTO.getType() && cartMarketResultByAdvenceDTO.getType() == 2) {
                CartMarketActivityResultDTO hmMarketActivityResult = cartMarketResultByAdvenceDTO.getMarketActivity();
                if (null != hmMarketActivityResult) {
                    if (!StringUtils.isBlank(hmMarketActivityResult.getRule())) {
                        if (FinalDatas.ZERO.equals(hmMarketActivityResult.getType())) {
                            stringbuider = new StringBuilder();
                            String[] split1 = hmMarketActivityResult.getRule().split(",");
                            for (String s : split1) {
                                String[] split = s.split("-");
                                stringbuider.append("满").append(split[0]).append("减").append(split[1]).append(",");
                                cartMarketResultByAdvenceDTO.setRule(stringbuider.toString());
                                cartMarketResultByAdvenceDTO.setStatus(hmMarketActivityResult.getStatus());
                            }

                            //去除末尾逗号
                            String marketBuider = stringbuider.toString();
                            if (!StringUtil.isBlank(marketBuider) && (",".equals(stringbuider.toString().substring(marketBuider.length() - 1, marketBuider.length())))) {
                                String activityMsg = stringbuider.toString().substring(0, stringbuider.toString().length() - 1);
                                cartMarketResultByAdvenceDTO.setRule("首单购买" + activityMsg.toString());
                            }

                        } else if (FinalDatas.ONE.equals(hmMarketActivityResult.getType())) {
                            stringbuider = new StringBuilder();
                            //活动描述
                            String rule = hmMarketActivityResult.getRule();
                            stringbuider.append("首单购买打").append(decimalFormat.format(Double.parseDouble(rule) * 10)).append("折");
                            cartMarketResultByAdvenceDTO.setRule(stringbuider.toString());
                            cartMarketResultByAdvenceDTO.setStatus(hmMarketActivityResult.getStatus());
                        }
                        //如果用户首单已经优惠.则不在计算
                        if (FinalDatas.ZERO == findCountByUid) {
                            //1是菜场打折.0是满减
                            if (FinalDatas.ONE == hmMarketActivityResult.getType()) {
                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                if (totalDisPriceByAdvence.compareTo(bigDecimal) > 0) {
                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                    //打折下来的值
                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                    totalDisPriceByAdvence = totalDisPriceByAdvence.subtract(subtract);
                                }
                            } else {
                                String[] split1 = hmMarketActivityResult.getRule().split(",");
                                for (int i = split1.length - 1; i >= 0; i--) {
                                    String[] split = split1[i].split("-");
                                    if (totalDisPriceByAdvence.compareTo(new BigDecimal(split[0])) >= 0) {
                                        totalDisPriceByAdvence = totalDisPriceByAdvence.subtract(new BigDecimal(split[1]));
                                        break;
                                    }
                                }
                            }
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(marketByAdvence);
                            if ((BigDecimal.ZERO.compareTo(totalDisPriceByAdvence)) >= 0) {
                                cartMarketResultByAdvenceDTO.setTotalPrice(FinalDatas.ZERO.toString());
                            } else {
                                cartMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                            }
                        } else {
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(marketByAdvence);
                            if ((BigDecimal.ZERO.compareTo(totalDisPriceByAdvence)) >= 0) {
                                cartMarketResultByAdvenceDTO.setTotalPrice(FinalDatas.ZERO.toString());
                            } else {
                                cartMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                            }
                        }
                    } else {
                        totalDisPriceByAdvence = totalDisPriceByAdvence.add(marketByAdvence);
                        if ((BigDecimal.ZERO.compareTo(totalDisPriceByAdvence)) >= 0) {
                            cartMarketResultByAdvenceDTO.setTotalPrice(FinalDatas.ZERO.toString());
                        } else {
                            cartMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                        }
                    }
                } else {
                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(marketByAdvence);
                    if ((BigDecimal.ZERO.compareTo(totalDisPriceByAdvence)) >= 0) {
                        cartMarketResultByAdvenceDTO.setTotalPrice(FinalDatas.ZERO.toString());
                    } else {
                        cartMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                    }
                }
            }
            if (null != cartMarketResultByCloseDTO.getType() && cartMarketResultByCloseDTO.getType() == 1) {
                CartMarketActivityResultDTO hmMarketActivityResult = cartMarketResultByCloseDTO.getMarketActivity();
                if (null != hmMarketActivityResult) {
                    if (!StringUtils.isBlank(hmMarketActivityResult.getRule())) {
                        if (FinalDatas.ZERO.equals(hmMarketActivityResult.getType())) {
                            stringbuider = new StringBuilder();
                            String[] split1 = hmMarketActivityResult.getRule().split(",");
                            for (String s : split1) {
                                String[] split = s.split("-");
                                stringbuider.append("满").append(split[0]).append("减").append(split[1]).append(",");
                                cartMarketResultByCloseDTO.setRule(stringbuider.toString());
                                cartMarketResultByCloseDTO.setStatus(hmMarketActivityResult.getStatus());
                            }

                            //去除末尾逗号
                            String marketBuider = stringbuider.toString();
                            if (!StringUtil.isBlank(marketBuider) && (",".equals(stringbuider.toString().substring(marketBuider.length() - 1, marketBuider.length())))) {
                                String activityMsg = stringbuider.toString().substring(0, stringbuider.toString().length() - 1);
                                cartMarketResultByCloseDTO.setRule("首单购买" + activityMsg.toString());
                            }

                        } else if (FinalDatas.ONE.equals(hmMarketActivityResult.getType())) {
                            stringbuider = new StringBuilder();
                            //活动描述
                            String rule = hmMarketActivityResult.getRule();
                            stringbuider.append("首单购买打").append(decimalFormat.format(Double.parseDouble(rule) * 10)).append("折");
                            cartMarketResultByCloseDTO.setRule(stringbuider.toString());
                            cartMarketResultByCloseDTO.setStatus(hmMarketActivityResult.getStatus());
                        }
                        //如果用户首单已经优惠.则不在计算
                        if (FinalDatas.ZERO == findCountByUid) {
                            //1是菜场打折.0是满减
                            if (FinalDatas.ONE == hmMarketActivityResult.getType()) {
                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                if (totalDisPriceByClose.compareTo(bigDecimal) > 0) {
                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                    //打折下来的值
                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                    totalDisPriceByClose = totalDisPriceByClose.subtract(subtract);
                                }
                            } else {
                                String[] split1 = hmMarketActivityResult.getRule().split(",");
                                for (int i = split1.length - 1; i >= 0; i--) {
                                    String[] split = split1[i].split("-");
                                    if (totalDisPriceByClose.compareTo(new BigDecimal(split[0])) >= 0) {
                                        totalDisPriceByClose = totalDisPriceByClose.subtract(new BigDecimal(split[1]));
                                        break;
                                    }
                                }
                            }
                            totalDisPriceByClose = totalDisPriceByClose.add(marketByClose);
                            if ((BigDecimal.ZERO.compareTo(totalDisPriceByClose)) >= 0) {
                                cartMarketResultByCloseDTO.setTotalPrice(FinalDatas.ZERO.toString());
                            } else {
                                cartMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                            }
                        } else {
                            totalDisPriceByClose = totalDisPriceByClose.add(marketByClose);
                            if ((BigDecimal.ZERO.compareTo(totalDisPriceByClose)) >= 0) {
                                cartMarketResultByCloseDTO.setTotalPrice(FinalDatas.ZERO.toString());
                            } else {
                                cartMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                            }
                        }
                    } else {
                        totalDisPriceByClose = totalDisPriceByClose.add(marketByClose);
                        if ((BigDecimal.ZERO.compareTo(totalDisPriceByClose)) >= 0) {
                            cartMarketResultByCloseDTO.setTotalPrice(FinalDatas.ZERO.toString());
                        } else {
                            cartMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                        }
                    }
                } else {
                    totalDisPriceByClose = totalDisPriceByClose.add(marketByClose);
                    if ((BigDecimal.ZERO.compareTo(totalDisPriceByClose)) >= 0) {
                        cartMarketResultByCloseDTO.setTotalPrice(FinalDatas.ZERO.toString());
                    } else {
                        cartMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                    }
                }
            }
            if (null != cartMarketResultByOpenDTO.getType() && cartMarketResultByOpenDTO.getType() == 0) {
                CartMarketActivityResultDTO hmMarketActivityResult = cartMarketResultByOpenDTO.getMarketActivity();
                if (null != hmMarketActivityResult) {
                    if (!StringUtils.isBlank(hmMarketActivityResult.getRule())) {
                        if (FinalDatas.ZERO.equals(hmMarketActivityResult.getType())) {
                            stringbuider = new StringBuilder();
                            String[] split1 = hmMarketActivityResult.getRule().split(",");
                            for (String s : split1) {
                                String[] split = s.split("-");
                                stringbuider.append("满").append(split[0]).append("减").append(split[1]).append(",");
                                cartMarketResultByOpenDTO.setRule(stringbuider.toString());
                                cartMarketResultByOpenDTO.setStatus(hmMarketActivityResult.getStatus());
                            }

                            //去除末尾逗号
                            String marketBuider = stringbuider.toString();
                            if (!StringUtil.isBlank(marketBuider) && (",".equals(stringbuider.toString().substring(marketBuider.length() - 1, marketBuider.length())))) {
                                String activityMsg = stringbuider.toString().substring(0, stringbuider.toString().length() - 1);
                                cartMarketResultByOpenDTO.setRule("首单购买" + activityMsg.toString());
                            }

                        } else if (FinalDatas.ONE.equals(hmMarketActivityResult.getType())) {
                            stringbuider = new StringBuilder();
                            //活动描述
                            String rule = hmMarketActivityResult.getRule();
                            stringbuider.append("首单购买打").append(decimalFormat.format(Double.parseDouble(rule) * 10)).append("折");
                            cartMarketResultByOpenDTO.setRule(stringbuider.toString());
                            cartMarketResultByOpenDTO.setStatus(hmMarketActivityResult.getStatus());
                        }
                        //如果用户首单已经优惠.则不在计算
                        if (FinalDatas.ZERO == findCountByUid) {
                            //1是菜场打折.0是满减
                            if (FinalDatas.ONE == hmMarketActivityResult.getType()) {
                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                if (totalDisPriceByOpen.compareTo(bigDecimal) > 0) {
                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                    //打折下来的值
                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                    totalDisPriceByOpen = totalDisPriceByOpen.subtract(subtract);
                                }
                            } else {
                                String[] split1 = hmMarketActivityResult.getRule().split(",");
                                for (int i = split1.length - 1; i >= 0; i--) {
                                    String[] split = split1[i].split("-");
                                    if (totalDisPriceByOpen.compareTo(new BigDecimal(split[0])) >= 0) {
                                        totalDisPriceByOpen = totalDisPriceByOpen.subtract(new BigDecimal(split[1]));
                                        break;
                                    }
                                }
                            }
                            totalDisPriceByOpen = totalDisPriceByOpen.add(marketByOpen);
                            if ((BigDecimal.ZERO.compareTo(totalDisPriceByOpen)) >= 0) {
                                cartMarketResultByOpenDTO.setTotalPrice(FinalDatas.ZERO.toString());
                            } else {
                                cartMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                            }
                        } else {
                            totalDisPriceByOpen = totalDisPriceByOpen.add(marketByOpen);
                            if ((BigDecimal.ZERO.compareTo(totalDisPriceByOpen)) >= 0) {
                                cartMarketResultByOpenDTO.setTotalPrice(FinalDatas.ZERO.toString());
                            } else {
                                cartMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                            }
                        }
                    } else {
                        totalDisPriceByOpen = totalDisPriceByOpen.add(marketByOpen);
                        if ((BigDecimal.ZERO.compareTo(totalDisPriceByOpen)) >= 0) {
                            cartMarketResultByOpenDTO.setTotalPrice(FinalDatas.ZERO.toString());
                        } else {
                            cartMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                        }
                    }
                } else {
                    totalDisPriceByOpen = totalDisPriceByOpen.add(marketByOpen);
                    if ((BigDecimal.ZERO.compareTo(totalDisPriceByOpen)) >= 0) {
                        cartMarketResultByOpenDTO.setTotalPrice(FinalDatas.ZERO.toString());
                    } else {
                        cartMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                    }
                }
            }


            //开张
            if (null != cartMarketResultByOpenDTO.getMartketId()) {
                cartMarketResultByOpenDTOList.add(cartMarketResultByOpenDTO);
                cartMarketResultListDTO.setOpen(cartMarketResultByOpenDTOList);
            } else {
                cartMarketResultListDTO.setOpen(cartMarketResultByOpenDTOList);
            }
            //预约
            if (null != cartMarketResultByAdvenceDTO.getMartketId()) {
                cartMarketResultByAdvenceDTOList.add(cartMarketResultByAdvenceDTO);
                cartMarketResultListDTO.setAdvance(cartMarketResultByAdvenceDTOList);
            } else {
                cartMarketResultListDTO.setAdvance(cartMarketResultByAdvenceDTOList);
            }
            //打烊
            if (null != cartMarketResultByCloseDTO.getMartketId()) {
                cartMarketResultByCloseDTOList.add(cartMarketResultByCloseDTO);
                cartMarketResultListDTO.setClose(cartMarketResultByCloseDTOList);
            } else {
                cartMarketResultListDTO.setClose(cartMarketResultByCloseDTOList);
            }
        }
        return ResultUtil.getSuccess(cartMarketResultListDTO);
    }

    @Override
    public void deleteGoodsOnShelves(Integer uid, Boolean flag) {
        //如果传来的flag为true,则删除购物车中已下架的商品.  如果传来的flag为false,则每调用10次删除一次购物车下架的商品
        if (true == flag) {
            this.hmBasketBeanDAO.executeDeleteMethod(uid, "deleteGoodsOnShelves");
        }
        if (false == flag) {
            Map<String, Integer> map = CountDTO.map;
            Integer integer = map.get(uid.toString());
            if (integer == null) {
                this.hmBasketBeanDAO.executeDeleteMethod(uid, "deleteGoodsOnShelves");
                map.put(uid.toString(), 1);
            } else if (integer != null && integer >= 10) {
                map.remove(uid.toString());
            } else {
                integer++;
                map.put(uid.toString(), integer);
            }
        }
    }
}


