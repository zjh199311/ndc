package com.zhongjian.service.hm.shopown.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.hm.basket.HmBasketBean;
import com.zhongjian.dao.entity.hm.market.HmMarketBean;
import com.zhongjian.dao.entity.hm.store.HmStoreActivityBean;
import com.zhongjian.dao.entity.hm.user.HmUserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.hm.basket.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.basket.result.HmBasketResultDTO;
import com.zhongjian.dto.hm.market.result.*;
import com.zhongjian.dto.hm.marketActivity.result.HmMarketActivityResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import com.zhongjian.dto.hm.storeActivity.result.HmStoreActivityResultDTO;
import com.zhongjian.service.hm.shopown.HmShopownService;
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
public class HmShopownServiceImpl extends HmBaseService<HmMarketBean, Integer> implements HmShopownService {

    private HmDAO<HmUserBean, Integer> hmDAO;

    private HmDAO<HmBasketBean, Integer> hmBasketBeanDAO;

    private HmDAO<HmStoreActivityBean, Integer> hmStoreActivityHmDAO;

    @Resource
    private void setHmStoreActivityHmDAO(HmDAO<HmStoreActivityBean, Integer> hmStoreActivityHmDAO) {
        this.hmStoreActivityHmDAO = hmStoreActivityHmDAO;
        this.hmStoreActivityHmDAO.setPerfix(HmStoreActivityBean.class.getName());
    }

    @Resource
    private void setHmBasketBeanDAO(HmDAO<HmBasketBean, Integer> hmBasketBeanDAO) {
        this.hmBasketBeanDAO = hmBasketBeanDAO;
        this.hmBasketBeanDAO.setPerfix(HmBasketBean.class.getName());
    }

    @Resource
    private void setHmDAO(HmDAO<HmUserBean, Integer> hmDAO) {
        this.hmDAO = hmDAO;
        this.hmDAO.setPerfix(HmUserBean.class.getName());
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
        List<HmMarketResultByAdvenceDTO> hmMarketResultByAdvenceDTOList = null;
        //打烊
        List<HmMarketResultByCloseDTO> marketResultByCloseDTOList = null;
        //开张
        List<HmMarketResultByOpenDTO> marketResultByOpenDTOList = null;
        StringBuilder stringbuider;
        HmBasketListQueryDTO hmBasketListQueryDTO;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        //将所有开张的总价相加.
        List<BigDecimal> listByOpen = null;
        //将所有预约的总价相加.
        List<BigDecimal> listByAdvence = null;
        //将所有打烊的总价相加.
        List<BigDecimal> listByClose = null;
        //将所有开张的优惠价格相加.
        BigDecimal priceByOpen = BigDecimal.ZERO;
        //将所有预约的优惠价格相加
        BigDecimal priceByAdvence = BigDecimal.ZERO;
        //将所有打烊的优惠价格相加
        BigDecimal priceByClose = BigDecimal.ZERO;
        //总价优惠后价格(预约)
        BigDecimal totalDisPriceByAdvence = BigDecimal.ZERO;
        //总价优惠后价格(开张)
        BigDecimal totalDisPriceByOpen = BigDecimal.ZERO;
        //总价优惠后价格(打烊)
        BigDecimal totalDisPriceByClose = BigDecimal.ZERO;
        //将所有开张的价格相加.
        BigDecimal numberByOpen = BigDecimal.ZERO;
        //将所有预约的价格相加
        BigDecimal numberByAdvence = BigDecimal.ZERO;
        //将所有打烊的价格相加
        BigDecimal numberByClose = BigDecimal.ZERO;
        //商家信息(预约)
        List<HmShopownResultDTO> hmShopownResultDTOListByAdvence = null;
        //商家信息(开张)
        List<HmShopownResultDTO> hmShopownResultDTOListByOpen = null;
        //商家信息(打烊)
        List<HmShopownResultDTO> hmShopownResultDTOListByClose = null;

        List<String> remarkList = null;
        //获取菜场信息
        List<HmMarketResultDTO> findMarketByUid = this.dao.executeListMethod(uid, "findMarketByUid", HmMarketResultDTO.class);
        for (HmMarketResultDTO hmShopownResultDTO : findMarketByUid) {
            //得到商户信息
            List<HmShopownResultDTO> hmShopownResultDTOS = hmShopownResultDTO.getHmShopownResultDTOS();
            for (HmShopownResultDTO shopownResultDTO : hmShopownResultDTOS) {
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
                hmBasketListQueryDTO = new HmBasketListQueryDTO();
                hmBasketListQueryDTO.setUid(uid);
                hmBasketListQueryDTO.setSid(shopownResultDTO.getPid());
                List<HmBasketResultDTO> findBasketById = this.hmBasketBeanDAO.executeListMethod(hmBasketListQueryDTO, "findBasketById", HmBasketResultDTO.class);
                remarkList = new ArrayList<String>();
                listByClose = new ArrayList<>();
                listByOpen = new ArrayList<>();
                listByAdvence = new ArrayList<>();
                for (HmBasketResultDTO hmBasketResultDTO : findBasketById) {
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
                    stringbuider = new StringBuilder();
                    stringbuider.append(hmBasketResultDTO.getUnitPrice()).append("元/").append(hmBasketResultDTO.getUnit());
                    hmBasketResultDTO.setUnitPrice(stringbuider.toString());
                    hmBasketResultDTO.setAmount(decimalFormat.format(Double.parseDouble(hmBasketResultDTO.getAmount())));
                    hmBasketResultDTO.setTotalPrice(decimalFormat.format(Double.parseDouble(hmBasketResultDTO.getTotalPrice())));
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
                                if (priceByAdvence.compareTo(new BigDecimal(hmStoreActivityResultDTO.getFull())) == 1) {
                                    priceByAdvence = priceByAdvence.subtract(reduce);
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
                    shopownResultDTO.setTotalPrice(numberByClose.toString());
                } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setTotalPrice(numberByOpen.toString());
                } else if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setTotalPrice(numberByAdvence.toString());
                }
                //这边封装DTO. 将开张预约打烊封装在对应的DTO里
                if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    if (null == hmMarketResultByAdvenceDTOList) {
                        hmMarketResultByAdvenceDTOList = new ArrayList<>();
                    }
                    if (null == hmShopownResultDTOListByAdvence) {
                        hmShopownResultDTOListByAdvence = new ArrayList<>();
                    }
                    //预约
                    HmMarketResultByAdvenceDTO hmMarketResultByAdvenceDTO = new HmMarketResultByAdvenceDTO();
                    hmMarketResultByAdvenceDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByAdvenceDTO.setMartketId(hmShopownResultDTO.getMartketId());
                    hmMarketResultByAdvenceDTO.setType(2);
                    hmMarketResultByAdvenceDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByAdvenceDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByAdvenceDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());
                    hmShopownResultDTOListByAdvence.add(shopownResultDTO);
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
                            //如果条件成立.  则用户在商户购买的产品没有优惠.那么直接判断总价在菜场是否有优惠
                            if (BigDecimal.ZERO.compareTo(priceByAdvence) > -1) {
                                if (numberByAdvence.compareTo(new BigDecimal(split[0])) == 1) {
                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence.subtract(new BigDecimal(split[1])));
                                } else {
                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                                }
                                //如果有优惠.那么就用优惠价去判断是否满足菜场的活动价格
                            } else {
                                if (priceByAdvence.compareTo(new BigDecimal(split[0])) == 1) {
                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence.subtract(new BigDecimal(split[1])));
                                } else {
                                    totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                                }
                            }
                            hmMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                        }
                    } else {
                        //这边则没有菜场活动价. 那么在看看是否有商家活动价.如果没有则直接给总价.
                        if (BigDecimal.ZERO.compareTo(priceByAdvence) > -1) {
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(numberByAdvence);
                        } else {
                            totalDisPriceByAdvence = totalDisPriceByAdvence.add(priceByAdvence);
                        }
                        hmMarketResultByAdvenceDTO.setTotalPrice(String.valueOf(totalDisPriceByAdvence.setScale(2)));
                    }

                    hmMarketResultByAdvenceDTOList.add(hmMarketResultByAdvenceDTO);
                    hmMarketResultListDTO.setHmMarketResultByAdvance(hmMarketResultByAdvenceDTOList);

                } else if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    marketResultByOpenDTOList = new ArrayList<>();
                    //开张
                    HmMarketResultByOpenDTO hmMarketResultByOpenDTO = new HmMarketResultByOpenDTO();
                    hmMarketResultByOpenDTO.setMartketId(hmShopownResultDTO.getMartketId());
                    hmMarketResultByOpenDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByOpenDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByOpenDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByOpenDTO.setType(0);
                    hmMarketResultByOpenDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());
                    if (null == hmShopownResultDTOListByOpen) {
                        hmShopownResultDTOListByOpen = new ArrayList<>();
                    }
                    //商家信息
                    hmShopownResultDTOListByOpen.add(shopownResultDTO);
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
                            //如果条件成立.  则用户在商户购买的产品没有优惠.那么直接判断总价在菜场是否有优惠
                            if (BigDecimal.ZERO.compareTo(priceByOpen) > -1) {
                                if (numberByOpen.compareTo(new BigDecimal(split[0])) == 1) {
                                    totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen.subtract(new BigDecimal(split[1])));
                                } else {
                                    totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                                }
                                //如果有优惠.那么就用优惠价去判断是否满足菜场的活动价格
                            } else {
                                if (priceByOpen.compareTo(new BigDecimal(split[0])) == 1) {
                                    totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen.subtract(new BigDecimal(split[1])));
                                } else {
                                    totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                                }
                            }
                            hmMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                        }
                    } else {
                        //这边则没有菜场活动价. 那么在看看是否有商家活动价.如果没有则直接给总价.
                        if (BigDecimal.ZERO.compareTo(priceByOpen) > -1) {
                            totalDisPriceByOpen = totalDisPriceByOpen.add(numberByOpen);
                        } else {
                            totalDisPriceByOpen = totalDisPriceByOpen.add(priceByOpen);
                        }
                        hmMarketResultByOpenDTO.setTotalPrice(String.valueOf(totalDisPriceByOpen.setScale(2)));
                    }

                    marketResultByOpenDTOList.add(hmMarketResultByOpenDTO);
                    hmMarketResultListDTO.setHmMarketResultByOpen(marketResultByOpenDTOList);
                } else {
                    if (null == marketResultByCloseDTOList) {
                        marketResultByCloseDTOList = new ArrayList<>();
                    }
                    //打烊
                    HmMarketResultByCloseDTO hmMarketResultByCloseDTO = new HmMarketResultByCloseDTO();
                    hmMarketResultByCloseDTO.setMartketId(hmShopownResultDTO.getMartketId());
                    hmMarketResultByCloseDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByCloseDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByCloseDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByCloseDTO.setType(1);
                    hmMarketResultByCloseDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());
                    if (null == hmShopownResultDTOListByClose) {
                        hmShopownResultDTOListByClose = new ArrayList<>();
                    }
                    hmShopownResultDTOListByClose.add(shopownResultDTO);
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
                            //如果条件成立.  则用户在商户购买的产品没有优惠.那么直接判断总价在菜场是否有优惠
                            if (BigDecimal.ZERO.compareTo(priceByClose) > -1) {
                                if (numberByClose.compareTo(new BigDecimal(split[0])) == 1) {
                                    totalDisPriceByClose = totalDisPriceByClose.add(numberByClose.subtract(new BigDecimal(split[1])));
                                } else {
                                    totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                                }
                                //如果有优惠.那么就用优惠价去判断是否满足菜场的活动价格
                            } else {
                                if (priceByClose.compareTo(new BigDecimal(split[0])) == 1) {
                                    totalDisPriceByClose = totalDisPriceByClose.add(priceByClose.subtract(new BigDecimal(split[1])));
                                } else {
                                    totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                                }
                            }
                            hmMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                        }
                    } else {
                        //这边则没有菜场活动价. 那么在看看是否有商家活动价.如果没有则直接给总价.
                        if (BigDecimal.ZERO.compareTo(priceByClose) > -1) {
                            totalDisPriceByClose = totalDisPriceByClose.add(numberByClose);
                        } else {
                            totalDisPriceByClose = totalDisPriceByClose.add(priceByClose);
                        }
                        hmMarketResultByCloseDTO.setTotalPrice(String.valueOf(totalDisPriceByClose.setScale(2)));
                    }
                    marketResultByCloseDTOList.add(hmMarketResultByCloseDTO);
                    hmMarketResultListDTO.setHmMarketResultByClose(marketResultByCloseDTOList);
                }
            }
        }
        return ResultUtil.getSuccess(hmMarketResultListDTO);
    }
}
