package com.zhongjian.service.cart.shopown.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.cart.CartParamDTO;
import com.zhongjian.dao.entity.cart.basket.CartBasketBean;
import com.zhongjian.dao.entity.cart.goods.CartGoodsBean;
import com.zhongjian.dao.entity.cart.market.CartMarketBean;
import com.zhongjian.dao.entity.cart.rider.CartRiderOrderBean;
import com.zhongjian.dao.entity.cart.store.CartStoreActivityBean;
import com.zhongjian.dao.entity.cart.user.UserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.cart.basket.query.HmBasketListQueryDTO;
import com.zhongjian.dto.cart.basket.result.HmBasketResultDTO;
import com.zhongjian.dto.cart.market.result.*;
import com.zhongjian.dto.cart.marketActivity.result.HmMarketActivityResultDTO;
import com.zhongjian.dto.cart.shopown.result.HmShopownResultDTO;
import com.zhongjian.dto.cart.storeActivity.result.HmStoreActivityResultDTO;
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
        ResultDTO<HmMarketResultListDTO> resultDTO = new ResultDTO<HmMarketResultListDTO>();
        if (null == uid) {
            return ResultUtil.getFail(CommonMessageEnum.UID_IS_NULL);
        }
        //组装打烊预约开张DTO
        HmMarketResultListDTO hmMarketResultListDTO = new HmMarketResultListDTO();
        //预约
        List<HmMarketResultByAdvenceDTO> hmMarketResultByAdvenceDTOList = new ArrayList<>();
        //打烊
        List<HmMarketResultByCloseDTO> hmMarketResultByCloseDTOList = new ArrayList<>();
        //开张
        List<HmMarketResultByOpenDTO> hmMarketResultByOpenDTOList = new ArrayList<>();
        StringBuilder stringbuider;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        //当前时间减去今天的最小时间并根据uid查询并且状态为待支付和支付成功时.该用户就不为首单了.那么该用户则不能被菜场优惠
        CartParamDTO cartParamDTO = new CartParamDTO();
        Long todayZeroTime = DateUtil.getTodayZeroTime();
        cartParamDTO.setCtime(todayZeroTime.intValue());
        cartParamDTO.setUid(uid);
        Integer findCountByUid = this.cartRiderOrderDAO.executeSelectOneMethod(cartParamDTO, "findCountByUid", Integer.class);

        //获取菜场信息
        List<HmMarketResultDTO> findMarketByUid = this.dao.executeListMethod(uid, "findMarketByUid", HmMarketResultDTO.class);
        for (HmMarketResultDTO hmShopownResultDTO : findMarketByUid) {
            //开张
            HmMarketResultByOpenDTO hmMarketResultByOpenDTO = new HmMarketResultByOpenDTO();
            //预约
            HmMarketResultByAdvenceDTO hmMarketResultByAdvenceDTO = new HmMarketResultByAdvenceDTO();
            //打烊
            HmMarketResultByCloseDTO hmMarketResultByCloseDTO = new HmMarketResultByCloseDTO();
            //商家信息(开张)
            List<HmShopownResultDTO> hmShopownResultDTOListByOpen = new ArrayList<>();
            //商家信息(预约)
            List<HmShopownResultDTO> hmShopownResultDTOListByAdvence = new ArrayList<>();
            //商家信息(打烊)
            List<HmShopownResultDTO> hmShopownResultDTOListByClose = new ArrayList<>();
            //得到商家信息
            List<HmShopownResultDTO> hmShopownResultDTOS = hmShopownResultDTO.getHmShopownResultDTOS();
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
            //打烊开关
            boolean flagByClose = true;
            //开张开关
            boolean flagByOpen = true;
            //预约
            boolean flagByAdvence = true;

            for (HmShopownResultDTO shopownResultDTO : hmShopownResultDTOS) {
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
                List<HmStoreActivityResultDTO> hmStoreActivityResultDTOS = shopownResultDTO.getHmStoreActivityResultDTOS();
                stringbuider = new StringBuilder();
                for (int i = hmStoreActivityResultDTOS.size() - 1; i >= 0; i--) {
                    HmStoreActivityResultDTO hmStoreActivityResultDTO = hmStoreActivityResultDTOS.get(i);
                    if (FinalDatas.ZERO == hmStoreActivityResultDTO.getType()) {
                        stringbuider.append(decimalFormat.format(Double.parseDouble(hmStoreActivityResultDTO.getFull()))).append("减").append(
                                decimalFormat.format(Double.parseDouble(hmStoreActivityResultDTO.getReduce()))
                        ).append(",");
                    }
                    if (FinalDatas.ONE == hmStoreActivityResultDTO.getType()) {
                        stringbuider.append(decimalFormat.format(new BigDecimal(hmStoreActivityResultDTO.getDiscount()).multiply(BigDecimal.TEN))).append("折");
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
                HmBasketListQueryDTO hmBasketListQueryDTO = new HmBasketListQueryDTO();
                hmBasketListQueryDTO.setUid(uid);
                hmBasketListQueryDTO.setSid(shopownResultDTO.getPid());
                List<HmBasketResultDTO> findBasketById = this.hmBasketBeanDAO.executeListMethod(hmBasketListQueryDTO, "findBasketById", HmBasketResultDTO.class);
                List<String> remarkList = new ArrayList<String>();
                //将所有开张的总价相加.
                List<BigDecimal> listByOpen = new ArrayList<>();
                //将所有预约的总价相加.
                List<BigDecimal> listByAdvence = new ArrayList<>();
                //将所有打烊的总价相加.
                List<BigDecimal> listByClose = new ArrayList<>();
                for (HmBasketResultDTO hmBasketResultDTO : findBasketById) {

                    //根据食品id得到食品信息
                    CartGoodsBean cartGoodsBean = this.cartGoodsBeanHmDAO.selectByPrimaryKey(hmBasketResultDTO.getGid());
                    if (null == cartGoodsBean) {
                        LogUtil.info("根据食品id得不到食品信息", "cartGoodsBean:" + cartGoodsBean);
                    } else {
                        stringbuider = new StringBuilder();
                        stringbuider.append(hmBasketResultDTO.getUnitPrice()).append("元/").append(cartGoodsBean.getUnit());
                        hmBasketResultDTO.setUnitPrice(stringbuider.toString());
                    }
                    hmBasketResultDTO.setAmount(decimalFormat.format(Double.parseDouble(hmBasketResultDTO.getAmount())));
                    hmBasketResultDTO.setTotalPrice(decimalFormat.format(Double.parseDouble(hmBasketResultDTO.getTotalPrice())));
                    if (FinalDatas.ZERO == hmBasketResultDTO.getGid()) {
                        hmBasketResultDTO.setFoodName("其他");
                        hmBasketResultDTO.setStatus(1);
                    } else {
                        hmBasketResultDTO.setFoodName(cartGoodsBean.getGname());
                    }
                    //判断要是没有remark备注就不需要拼接组装;
                    if (!StringUtils.isBlank(hmBasketResultDTO.getRemark())) {
                        //拼接备注信息:[名字]+备注信息
                        String remart = ("[") + (hmBasketResultDTO.getFoodName()) + ("]") + (hmBasketResultDTO.getRemark());
                        remarkList.add(remart);
                    }
                    //将用户在商品下的所有总价存入list里面.
                    if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                        listByClose.add(new BigDecimal(hmBasketResultDTO.getTotalPrice()));
                    } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                        listByOpen.add(new BigDecimal(hmBasketResultDTO.getTotalPrice()));
                    } else if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                        listByAdvence.add(new BigDecimal(hmBasketResultDTO.getTotalPrice()));
                    }
                }
                //备注信息
                shopownResultDTO.setRemarkList(remarkList);
                //该用户在商家下对应的食品信息
                shopownResultDTO.setHmBasketResultDTOS(findBasketById);
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
                List<HmStoreActivityResultDTO> findStoreActivityBySid = this.hmStoreActivityHmDAO.executeListMethod(shopownResultDTO.getPid(), "findStoreActivityBySid", HmStoreActivityResultDTO.class);
                if (CollectionUtils.isEmpty(findStoreActivityBySid)) {
                    LogUtil.info("该商家没有优惠活动", "findStoreActivityBySid" + findStoreActivityBySid);
                } else {
                    for (HmStoreActivityResultDTO hmStoreActivityResultDTO : findStoreActivityBySid) {
                        //折扣的优惠
                        if (FinalDatas.ONE == hmStoreActivityResultDTO.getType()) {
                            if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                                priceByClose = numberByClose.multiply(new BigDecimal(hmStoreActivityResultDTO.getDiscount()));
                            } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                                priceByOpen = numberByOpen.multiply(new BigDecimal(hmStoreActivityResultDTO.getDiscount()));
                            } else {
                                priceByAdvence = numberByAdvence.multiply(new BigDecimal(hmStoreActivityResultDTO.getDiscount()));
                            }
                        }
                        //满减的优惠
                        if (FinalDatas.ZERO == hmStoreActivityResultDTO.getType()) {
                            //根据查询数据库倒序排列判断如果大于1则总价大于满减价那么就减去优惠值
                            //优惠价
                            BigDecimal reduce = new BigDecimal(hmStoreActivityResultDTO.getReduce());
                            if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                                if (numberByClose.compareTo(new BigDecimal(hmStoreActivityResultDTO.getFull())) == 1) {
                                    priceByClose = numberByClose.subtract(reduce);
                                    break;
                                }
                            } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                                if (numberByOpen.compareTo(new BigDecimal(hmStoreActivityResultDTO.getFull())) == 1) {
                                    priceByOpen = numberByOpen.subtract(reduce);
                                    break;
                                }
                            } else {
                                if (numberByAdvence.compareTo(new BigDecimal(hmStoreActivityResultDTO.getFull())) == 1) {
                                    priceByAdvence = numberByAdvence.subtract(reduce);
                                    break;
                                }
                            }

                        }
                    }
                    //商户下的总优惠价
                    if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                        shopownResultDTO.setDiscountPrice(String.valueOf(priceByClose.setScale(2)));
                    } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                        shopownResultDTO.setDiscountPrice(String.valueOf(priceByOpen.setScale(2)));
                    } else if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                        shopownResultDTO.setDiscountPrice(String.valueOf(priceByAdvence.setScale(2)));
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
                    hmMarketResultByAdvenceDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByAdvenceDTO.setType(2);
                    hmMarketResultByAdvenceDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByAdvenceDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByAdvenceDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());

                    //组装菜场DTO 如果一个菜场对应各自的商户.如果没有值则添加如果有值则判断该商户是否为该菜场旗下.
                    if (null == hmMarketResultByAdvenceDTO.getMartketId()) {
                        hmMarketResultByAdvenceDTO.setMartketId(hmShopownResultDTO.getMartketId());
                        hmShopownResultDTOListByAdvence.add(shopownResultDTO);
                    } else {
                        if (hmShopownResultDTO.getMartketId() == hmMarketResultByAdvenceDTO.getMartketId()) {
                            hmShopownResultDTOListByAdvence.add(shopownResultDTO);
                        } else {
                            hmShopownResultDTOListByAdvence = new ArrayList<>();
                            hmShopownResultDTOListByAdvence.add(shopownResultDTO);
                        }
                    }
                    hmMarketResultByAdvenceDTO.setHmShopownResultDTOS(hmShopownResultDTOListByAdvence);
                    HmMarketActivityResultDTO hmMarketActivityResult = hmMarketResultByAdvenceDTO.getHmMarketActivityResultDTO();
                    if (null != hmMarketActivityResult) {
                        if (!StringUtils.isBlank(hmMarketActivityResult.getRule())) {
                            String[] split = hmMarketActivityResult.getRule().split("-");
                            if (FinalDatas.ZERO.equals(hmMarketActivityResult.getType())) {
                                stringbuider = new StringBuilder();
                                stringbuider.append("首单购买满").append(split[0]).append("减").append(split[1]);
                                hmMarketResultByAdvenceDTO.setRule(stringbuider.toString());
                                hmMarketResultByAdvenceDTO.setStatus(hmMarketActivityResult.getStatus());
                            }
                            if (FinalDatas.ONE.equals(hmMarketActivityResult.getType())) {
                                stringbuider = new StringBuilder();
                                //活动描述
                                String rule = hmMarketActivityResult.getRule();
                                stringbuider.append("首单购买打").append(decimalFormat.format(Double.parseDouble(rule) * 10)).append("折");
                                hmMarketResultByAdvenceDTO.setRule(stringbuider.toString());
                                hmMarketResultByAdvenceDTO.setStatus(hmMarketActivityResult.getStatus());
                            }
                            //如果用户首单已经优惠.则不在计算
                            if (FinalDatas.ZERO == findCountByUid) {
                                //在判断该商家是否在菜场下有优惠. 要是有优惠则根据总价来判断是否满足优惠要是不满足则根据下一个商家的总价加来判断
                                //满足则优惠不满足就不优惠.
                                if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getUnFavorable())) {
                                    //1是菜场打折.0是满减
                                    if (FinalDatas.ONE == hmMarketActivityResult.getType()) {
                                        //如果有商户优惠则计算总价是否存在菜场的优惠中.如果有则乘于菜场优惠折扣
                                        if (BigDecimal.ZERO.compareTo(priceByAdvence) < 0) {
                                            if (flagByAdvence) {
                                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                                marketByAdvence = marketByAdvence.add(numberByAdvence);
                                                if (marketByAdvence.compareTo(bigDecimal) > 0) {
                                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                                    //打折下来的值
                                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                                    priceByAdvence = priceByAdvence.subtract(subtract);
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                                    flagByAdvence = false;
                                                } else {
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                                }
                                            } else {
                                                totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                            }
                                        } else {
                                            marketByAdvence = marketByAdvence.add(numberByAdvence);
                                            if (numberByAdvence.compareTo(new BigDecimal(hmMarketActivityResult.getUpLimit())) > 0) {
                                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                                if (numberByAdvence.compareTo(bigDecimal) > 0) {
                                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                                    //打折下来的值
                                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                                    numberByAdvence = numberByAdvence.subtract(subtract);
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                                                    flagByAdvence = false;
                                                } else {
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                                                }
                                            } else {
                                                totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                                            }
                                        }
                                    } else {
                                        //如果有商户优惠则计算总价是否存在菜场的优惠中.如果有则减去菜场优惠价格.
                                        if (BigDecimal.ZERO.compareTo(priceByAdvence) < 0) {
                                            marketByAdvence = marketByAdvence.add(numberByAdvence);
                                            if (marketByAdvence.compareTo(new BigDecimal(split[0])) > 0) {
                                                if (flagByAdvence) {
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence.subtract(new BigDecimal(split[1])));
                                                    flagByAdvence = false;
                                                } else {
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                                }
                                            } else {
                                                totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                            }
                                        } else {
                                            marketByAdvence = marketByAdvence.add(numberByAdvence);
                                            if (marketByAdvence.compareTo(new BigDecimal(split[0])) > 0) {
                                                if (flagByAdvence) {
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence.subtract(new BigDecimal(split[1])));
                                                    flagByAdvence = false;
                                                } else {
                                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (BigDecimal.ZERO.compareTo(priceByAdvence) == 0) {
                                        totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                                    } else {
                                        totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                    }
                                }
                            } else {
                                if (BigDecimal.ZERO.compareTo(priceByAdvence) == 0) {
                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                                } else {
                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                }
                            }
                            hmMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                        }
                    } else {
                        //这边则没有菜场活动价. 那么在看看是否有商家活动价.如果没有则直接给总价.
                        if (BigDecimal.ZERO.compareTo(priceByAdvence) == 0) {
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                        } else {
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                        }
                        hmMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                    }
                } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    hmMarketResultByOpenDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByOpenDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByOpenDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByOpenDTO.setType(0);
                    hmMarketResultByOpenDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());

                    //组装菜场DTO 如果一个菜场对应各自的商户.如果没有值则添加如果有值则判断该商户是否为该菜场旗下.
                    if (null == hmMarketResultByOpenDTO.getMartketId()) {
                        hmMarketResultByOpenDTO.setMartketId(hmShopownResultDTO.getMartketId());
                        hmShopownResultDTOListByOpen.add(shopownResultDTO);
                    } else {
                        if (hmShopownResultDTO.getMartketId() == hmMarketResultByOpenDTO.getMartketId()) {
                            hmShopownResultDTOListByOpen.add(shopownResultDTO);
                        } else {
                            hmShopownResultDTOListByOpen = new ArrayList<>();
                            hmShopownResultDTOListByOpen.add(shopownResultDTO);
                        }
                    }
                    hmMarketResultByOpenDTO.setHmShopownResultDTOS(hmShopownResultDTOListByOpen);
                    HmMarketActivityResultDTO hmMarketActivityResult = hmMarketResultByOpenDTO.getHmMarketActivityResultDTO();
                    if (null != hmMarketActivityResult) {
                        if (!StringUtils.isBlank(hmMarketActivityResult.getRule())) {
                            String[] split = hmMarketActivityResult.getRule().split("-");
                            if (FinalDatas.ZERO.equals(hmMarketActivityResult.getType())) {
                                stringbuider = new StringBuilder();
                                stringbuider.append("首单购买满").append(split[0]).append("减").append(split[1]);
                                hmMarketResultByOpenDTO.setRule(stringbuider.toString());
                                hmMarketResultByOpenDTO.setStatus(hmMarketActivityResult.getStatus());
                            }
                            if (FinalDatas.ONE.equals(hmMarketActivityResult.getType())) {
                                stringbuider = new StringBuilder();
                                //活动描述
                                String rule = hmMarketActivityResult.getRule();
                                stringbuider.append("首单购买打").append(decimalFormat.format(Double.parseDouble(rule) * 10)).append("折");
                                hmMarketResultByOpenDTO.setRule(stringbuider.toString());
                                hmMarketResultByOpenDTO.setStatus(hmMarketActivityResult.getStatus());
                            }
                            //如果用户首单已经优惠.则不在计算
                            if (FinalDatas.ZERO == findCountByUid) {
                                //在判断该商家是否在菜场下有优惠. 要是有优惠则根据总价来判断是否满足优惠要是不满足则根据下一个商家的总价加来判断
                                //满足则优惠不满足就不优惠.
                                if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getUnFavorable())) {
                                    //1是菜场打折.0是满减
                                    if (FinalDatas.ONE == hmMarketActivityResult.getType()) {
                                        //如果有商户优惠则计算总价是否存在菜场的优惠中.如果有则乘于菜场优惠折扣
                                        if (BigDecimal.ZERO.compareTo(priceByOpen) < 0) {
                                            if (flagByOpen) {
                                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                                marketByAdvence = marketByAdvence.add(numberByOpen);
                                                if (marketByAdvence.compareTo(bigDecimal) > 0) {
                                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                                    //打折下来的值
                                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                                    priceByOpen = priceByOpen.subtract(subtract);
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                                    flagByOpen = false;
                                                } else {
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                                }
                                            } else {
                                                totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                            }
                                        } else {
                                            marketByOpen = marketByOpen.add(numberByOpen);
                                            if (numberByOpen.compareTo(new BigDecimal(hmMarketActivityResult.getUpLimit())) > 0) {
                                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                                if (numberByOpen.compareTo(bigDecimal) > 0) {
                                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                                    //打折下来的值
                                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                                    numberByOpen = numberByOpen.subtract(subtract);
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                                                    flagByOpen = false;
                                                } else {
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                                                }
                                            } else {
                                                totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                                            }
                                        }
                                    } else {
                                        //如果有商户优惠则计算总价是否存在菜场的优惠中.如果有则减去菜场优惠价格.
                                        if (BigDecimal.ZERO.compareTo(priceByOpen) < 0) {
                                            marketByOpen = marketByOpen.add(numberByOpen);
                                            if (marketByOpen.compareTo(new BigDecimal(split[0])) > 0) {
                                                if (flagByAdvence) {
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen.subtract(new BigDecimal(split[1])));
                                                    flagByAdvence = false;
                                                } else {
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                                }
                                            } else {
                                                totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                            }
                                        } else {
                                            marketByOpen = marketByOpen.add(numberByOpen);
                                            if (marketByOpen.compareTo(new BigDecimal(split[0])) > 0) {
                                                if (flagByOpen) {
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen.subtract(new BigDecimal(split[1])));
                                                    flagByOpen = false;
                                                } else {
                                                    totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (BigDecimal.ZERO.compareTo(priceByOpen) == 0) {
                                        totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                                    } else {
                                        totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                    }
                                }
                            } else {
                                if (BigDecimal.ZERO.compareTo(priceByOpen) == 0) {
                                    totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                                } else {
                                    totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                }
                            }
                            hmMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                        }
                    } else {
                        //这边则没有菜场活动价. 那么在看看是否有商家活动价.如果没有则直接给总价.
                        if (BigDecimal.ZERO.compareTo(priceByOpen) == 0) {
                            totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                        } else {
                            totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                        }
                        hmMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                    }
                } else {
                    hmMarketResultByCloseDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByCloseDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByCloseDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByCloseDTO.setType(1);
                    hmMarketResultByCloseDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());

                    //组装菜场DTO 如果一个菜场对应各自的商户.如果没有值则添加如果有值则判断该商户是否为该菜场旗下.
                    if (null == hmMarketResultByCloseDTO.getMartketId()) {
                        hmMarketResultByCloseDTO.setMartketId(hmShopownResultDTO.getMartketId());
                        hmShopownResultDTOListByClose.add(shopownResultDTO);
                    } else {
                        if (hmShopownResultDTO.getMartketId() == hmMarketResultByCloseDTO.getMartketId()) {
                            hmShopownResultDTOListByClose.add(shopownResultDTO);
                        } else {
                            hmShopownResultDTOListByClose = new ArrayList<>();
                            hmShopownResultDTOListByClose.add(shopownResultDTO);
                        }
                    }
                    hmMarketResultByCloseDTO.setHmShopownResultDTOS(hmShopownResultDTOListByClose);
                    HmMarketActivityResultDTO hmMarketActivityResult = hmMarketResultByCloseDTO.getHmMarketActivityResultDTO();
                    if (null != hmMarketActivityResult) {
                        if (!StringUtils.isBlank(hmMarketActivityResult.getRule())) {
                            String[] split = hmMarketActivityResult.getRule().split("-");
                            if (FinalDatas.ZERO.equals(hmMarketActivityResult.getType())) {
                                stringbuider = new StringBuilder();
                                stringbuider.append("首单购买满").append(split[0]).append("减").append(split[1]);
                                hmMarketResultByCloseDTO.setRule(stringbuider.toString());
                                hmMarketResultByCloseDTO.setStatus(hmMarketActivityResult.getStatus());
                            }
                            if (FinalDatas.ONE.equals(hmMarketActivityResult.getType())) {
                                stringbuider = new StringBuilder();
                                //活动描述
                                String rule = hmMarketActivityResult.getRule();
                                stringbuider.append("首单购买打").append(decimalFormat.format(Double.parseDouble(rule) * 10)).append("折");
                                hmMarketResultByCloseDTO.setRule(stringbuider.toString());
                                hmMarketResultByCloseDTO.setStatus(hmMarketActivityResult.getStatus());
                            }
                            //如果用户首单已经优惠.则不在计算
                            if (FinalDatas.ZERO == findCountByUid) {
                                //在判断该商家是否在菜场下有优惠. 要是有优惠则根据总价来判断是否满足优惠要是不满足则根据下一个商家的总价加来判断
                                //满足则优惠不满足就不优惠.
                                if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getUnFavorable())) {
                                    //1是菜场打折.0是满减
                                    if (FinalDatas.ONE == hmMarketActivityResult.getType()) {
                                        //如果有商户优惠则计算总价是否存在菜场的优惠中.如果有则乘于菜场优惠折扣
                                        if (BigDecimal.ZERO.compareTo(priceByClose) < 0) {
                                            if (flagByClose) {
                                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                                marketByClose = marketByClose.add(numberByClose);
                                                if (marketByClose.compareTo(bigDecimal) > 0) {
                                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                                    //打折下来的值
                                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                                    priceByClose = priceByClose.subtract(subtract);
                                                    totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                                                    flagByClose = false;
                                                } else {
                                                    totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                                                }
                                            } else {
                                                totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                                            }
                                        } else {
                                            marketByClose = marketByClose.add(numberByClose);
                                            if (numberByClose.compareTo(new BigDecimal(hmMarketActivityResult.getUpLimit())) > 0) {
                                                BigDecimal bigDecimal = new BigDecimal(hmMarketActivityResult.getUpLimit());
                                                if (numberByClose.compareTo(bigDecimal) > 0) {
                                                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hmMarketActivityResult.getRule()));
                                                    //打折下来的值
                                                    BigDecimal subtract = bigDecimal.subtract(multiply);
                                                    numberByClose = numberByClose.subtract(subtract);
                                                    totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                                                    flagByClose = false;
                                                } else {
                                                    totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                                                }
                                            } else {
                                                totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                                            }
                                        }
                                    } else {
                                        //如果有商户优惠则计算总价是否存在菜场的优惠中.如果有则减去菜场优惠价格.
                                        if (BigDecimal.ZERO.compareTo(priceByClose) < 0) {
                                            marketByClose = marketByClose.add(numberByClose);
                                            if (marketByClose.compareTo(new BigDecimal(split[0])) > 0) {
                                                if (flagByClose) {
                                                    totalDisPriceByClose = totalDisPriceByClose.add(priceByClose.subtract(new BigDecimal(split[1])));
                                                    flagByClose = false;
                                                } else {
                                                    totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                                                }
                                            } else {
                                                totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                                            }
                                        } else {
                                            marketByClose = marketByClose.add(numberByClose);
                                            if (marketByClose.compareTo(new BigDecimal(split[0])) > 0) {
                                                if (flagByClose) {
                                                    totalDisPriceByClose = totalDisPriceByClose.add(numberByClose.subtract(new BigDecimal(split[1])));
                                                    flagByClose = false;
                                                } else {
                                                    totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (BigDecimal.ZERO.compareTo(priceByClose) == 0) {
                                        totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                                    } else {
                                        totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                                    }
                                }
                            } else {
                                if (BigDecimal.ZERO.compareTo(priceByOpen) == 0) {
                                    totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                                } else {
                                    totalDisPriceByClose = totalDisPriceByClose.add(priceByOpen);
                                }
                            }
                            hmMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                        }
                    } else {
                        //这边则没有菜场活动价. 那么在看看是否有商家活动价.如果没有则直接给总价.
                        if (BigDecimal.ZERO.compareTo(priceByOpen) == 0) {
                            totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                        } else {
                            totalDisPriceByClose = totalDisPriceByClose.add(priceByOpen);
                        }
                        hmMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                    }
                }
            }
//            //开张
//            if (null != hmMarketResultByOpenDTO.getMartketId()) {
//                hmMarketResultByOpenDTOList.add(hmMarketResultByOpenDTO);
//                hmMarketResultListDTO.setHmMarketResultByOpen(hmMarketResultByOpenDTOList);
//            }
            if (null != hmMarketResultByAdvenceDTO.getMartketId()) {
                //预约
                hmMarketResultByAdvenceDTOList.add(hmMarketResultByAdvenceDTO);
                hmMarketResultListDTO.setHmMarketResultByAdvance(hmMarketResultByAdvenceDTOList);
            }
//            //打烊
//            if (null != hmMarketResultByCloseDTO.getMartketId()) {
//                hmMarketResultByCloseDTOList.add(hmMarketResultByCloseDTO);
//                hmMarketResultListDTO.setHmMarketResultByClose(hmMarketResultByCloseDTOList);
//            }
        }
        return ResultUtil.getSuccess(hmMarketResultListDTO);
    }
}
