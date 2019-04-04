package com.zhongjian.service.hm.shopown.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.common.constant.enums.hm.basket.HmBasketEnum;
import com.zhongjian.common.constant.enums.response.CommonMessageEnum;
import com.zhongjian.dao.entity.hm.basket.HmBasketBean;
import com.zhongjian.dao.entity.hm.market.HmMarketBean;
import com.zhongjian.dao.entity.hm.store.HmStoreActivityBean;
import com.zhongjian.dao.entity.hm.user.HmUserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.basket.result.HmBasketResultDTO;
import com.zhongjian.dto.hm.market.result.*;
import com.zhongjian.dto.hm.marketActivity.result.HmMarketActivityResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import com.zhongjian.dto.hm.storeActivity.result.HmStoreActivityResultDTO;
import com.zhongjian.service.hm.shopown.HmShopownService;
import com.zhongjian.util.LogUtil;
import com.zhongjian.util.StringUtil;
import org.springframework.beans.BeanUtils;
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
    public ResultDTO<HmMarketResultListDTO> queryList(String loginToken) {
        ResultDTO<HmMarketResultListDTO> resultDTO = new ResultDTO<HmMarketResultListDTO>();
        if (StringUtils.isBlank(loginToken)) {
            resultDTO.setErrorMessage(HmBasketEnum.LOGINTOKEN_IS_NULL.getMsg());
            resultDTO.setStatusCode(HmBasketEnum.LOGINTOKEN_IS_NULL.getCode());
            return resultDTO;
        }
        //获取uid
        Integer uid = this.hmDAO.executeSelectOneMethod(loginToken, "findUidByLoginToken", Integer.class);
        LogUtil.info("获取uid", "uid:" + uid);

        List<HmMarketResultDTO> findMarketByUid = this.dao.executeListMethod(uid, "findMarketByUid", HmMarketResultDTO.class);
        //组装打烊预约开张DTO
        HmMarketResultListDTO hmMarketResultListDTO = new HmMarketResultListDTO();
        //预约
        List<HmMarketResultByAdvenceDTO> hmMarketResultByAdvenceDTOList = null;
        //打烊
        List<HmMarketResultByCloseDTO> marketResultByCloseDTOList = null;
        //开张
        List<HmMarketResultByOpenDTO> marketResultByOpenDTOList = null;

        StringBuffer stringBuffer;
        HmMarketActivityResultDTO hmMarketActivityResultDTO;
        HmBasketListQueryDTO hmBasketListQueryDTO;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        List<BigDecimal> list;
        List<String> remarkList = null;
        //将所有优惠价格相加.
        BigDecimal disPrice = BigDecimal.ZERO;
        //总价优惠后价格
        BigDecimal totalDisPrice = BigDecimal.ZERO;
        //用户在商户下购买的产品总价格
        BigDecimal number = BigDecimal.ZERO;
        //用户在购物车的总价格
        BigDecimal totalNumber = BigDecimal.ZERO;
        for (HmMarketResultDTO hmShopownResultDTO : findMarketByUid) {

            //得到商户信息
            List<HmShopownResultDTO> hmShopownResultDTOS = hmShopownResultDTO.getHmShopownResultDTOS();
            for (HmShopownResultDTO shopownResultDTO : hmShopownResultDTOS) {
                number = BigDecimal.ZERO;
                list = new ArrayList<BigDecimal>();
                if (FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setStatusMsg("开张");
                }
                if (FinalDatas.ONE.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setStatusMsg("打烊");
                }
                if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    shopownResultDTO.setStatusMsg("预约中");
                }
                //每个商户下的活动信息
                List<HmStoreActivityResultDTO> hmStoreActivityResultDTOS = shopownResultDTO.getHmStoreActivityResultDTOS();
                stringBuffer = new StringBuffer();
                for (int i = hmStoreActivityResultDTOS.size() - 1; i >= 0; i--) {
                    HmStoreActivityResultDTO hmStoreActivityResultDTO = hmStoreActivityResultDTOS.get(i);
                    if (FinalDatas.ZERO == hmStoreActivityResultDTO.getType()) {
                        stringBuffer.append(decimalFormat.format(Double.parseDouble(hmStoreActivityResultDTO.getFull()))).append("减").append(
                                decimalFormat.format(Double.parseDouble(hmStoreActivityResultDTO.getReduce()))
                        ).append(",");
                    }
                    if (FinalDatas.ONE == hmStoreActivityResultDTO.getType()) {
                        stringBuffer.append(decimalFormat.format(new BigDecimal(hmStoreActivityResultDTO.getDiscount()).multiply(BigDecimal.TEN))).append("折");
                    }
                }
                //去除末尾逗号
                String buffer = stringBuffer.toString();

                if (!StringUtil.isBlank(buffer) && (",".equals(stringBuffer.toString().substring(buffer.length() - 1, buffer.length())))) {
                    String activityMsg = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
                    shopownResultDTO.setActivityMsg(activityMsg);
                } else if (!StringUtil.isBlank(buffer)) {
                    shopownResultDTO.setActivityMsg(buffer.toString());
                }
                //这个商户下用户购买的食品信息
                hmBasketListQueryDTO = new HmBasketListQueryDTO();
                hmBasketListQueryDTO.setUid(uid);
                hmBasketListQueryDTO.setSid(shopownResultDTO.getPid());
                List<HmBasketResultDTO> findBasketById = this.hmBasketBeanDAO.executeListMethod(hmBasketListQueryDTO, "findBasketById", HmBasketResultDTO.class);
                remarkList = new ArrayList<String>();
                for (HmBasketResultDTO hmBasketResultDTO : findBasketById) {
                    //判断要是没有remark备注就不需要拼接组装;
                    if (!StringUtils.isBlank(hmBasketResultDTO.getRemark())) {
                        //拼接备注信息:[名字]+备注信息
                        String remart = ("[") + (hmBasketResultDTO.getFoodName()) + ("]") + (hmBasketResultDTO.getRemark());
                        remarkList.add(remart);
                    }
                    //将用户在商品下的所有总价存入list里面.
                    list.add(new BigDecimal(hmBasketResultDTO.getTotalPrice()));
                    stringBuffer = new StringBuffer();
                    stringBuffer.append(hmBasketResultDTO.getUnitPrice()).append("元/").append(hmBasketResultDTO.getUnit());
                    hmBasketResultDTO.setUnitPrice(stringBuffer.toString());
                    hmBasketResultDTO.setAmount(decimalFormat.format(Double.parseDouble(hmBasketResultDTO.getAmount())));
                    hmBasketResultDTO.setTotalPrice(decimalFormat.format(Double.parseDouble(hmBasketResultDTO.getTotalPrice())));
                }
                //该用户在商家下对应的食品信息
                shopownResultDTO.setHmBasketResultDTOS(findBasketById);
                //循环list获取商品总价并相加.
                for (int i = 0; i < list.size(); i++) {
                    number = list.get(i).add(number);
                }
                totalNumber = totalNumber.add(number);
                /**
                 *   这边逻辑. 因为商品的总价是放在商户的bean中. 而活动优惠价格的bean放在商户bean中的.  所有要先计算完总价才去判断该商户是否有活动.因为该活动表中的
                 *   full字段.在这边取不到.写在上面的活动循环中就得不到总值.这边就根据pid去查询数据库
                 */
                List<HmStoreActivityResultDTO> findStoreActivityBySid = this.hmStoreActivityHmDAO.executeListMethod(shopownResultDTO.getPid(), "findStoreActivityBySid", HmStoreActivityResultDTO.class);
                if (CollectionUtils.isEmpty(findStoreActivityBySid)) {
                    LogUtil.info("该商家没有优惠活动", "findStoreActivityBySid" + findStoreActivityBySid);
                } else {
                    BigDecimal discount = BigDecimal.ZERO;
                    BigDecimal multiply = BigDecimal.ZERO;
                    new ArrayList<>();
                    for (HmStoreActivityResultDTO hmStoreActivityResultDTO : findStoreActivityBySid) {
                        //折扣的优惠
                        if (FinalDatas.ONE == hmStoreActivityResultDTO.getType()) {
                            //相乘后的价格
                            multiply = number.multiply(new BigDecimal(hmStoreActivityResultDTO.getDiscount()));
                            disPrice = disPrice.add(multiply);
                        }
                        //满减的优惠
                        if (FinalDatas.ZERO == hmStoreActivityResultDTO.getType()) {
                            //根据查询数据库倒序排列判断如果大于1则总价大于满减价那么就减去优惠值
                            if (number.compareTo(new BigDecimal(hmStoreActivityResultDTO.getFull())) == 1) {
                                //优惠价
                                BigDecimal reduce = new BigDecimal(hmStoreActivityResultDTO.getReduce());
                                multiply = number.subtract(reduce);
                                disPrice = disPrice.add(multiply);
                                break;
                            }
                        }
                    }
                    shopownResultDTO.setDiscountPrice(String.valueOf(multiply.setScale(2)));
                }
                shopownResultDTO.setTotalPrice(number.toString());
                shopownResultDTO.setRemarkList(remarkList);
                if (FinalDatas.TWO.toString().equals(shopownResultDTO.getStatus())) {
                    if(hmMarketResultByAdvenceDTOList==null){
                        hmMarketResultByAdvenceDTOList = new ArrayList<>();
                    }
                    //商家信息
                    List<HmShopownResultDTO> hmShopownResultDTOList =new ArrayList<>();
                    //预约
                    HmMarketResultByAdvenceDTO hmMarketResultByAdvenceDTO = new HmMarketResultByAdvenceDTO();
                    HmShopownResultDTO hmShopownResult = new HmShopownResultDTO();
                    BeanUtils.copyProperties(shopownResultDTO,hmShopownResult);
                    hmShopownResultDTOList.add(hmShopownResult);
                    hmMarketResultByAdvenceDTO.setHmShopownResultDTOS(hmShopownResultDTOList);
                    hmMarketResultByAdvenceDTOList.add(hmMarketResultByAdvenceDTO);
                    hmMarketResultByAdvenceDTO.setMartketId(hmShopownResultDTO.getMartketId());
                    hmMarketResultByAdvenceDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByAdvenceDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByAdvenceDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByAdvenceDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());
                }else if(FinalDatas.ZERO.toString().equals(shopownResultDTO.getStatus())){
                    if(marketResultByOpenDTOList==null){
                        marketResultByOpenDTOList = new ArrayList<>();
                    }
                    //商家信息
                    List<HmShopownResultDTO> hmShopownResultDTOList =new ArrayList<>();
                    //开张
                    HmMarketResultByOpenDTO hmMarketResultByOpenDTO = new HmMarketResultByOpenDTO();
                    HmShopownResultDTO hmShopownResult = new HmShopownResultDTO();
                    BeanUtils.copyProperties(shopownResultDTO,hmShopownResult);
                    hmShopownResultDTOList.add(hmShopownResult);
                    hmMarketResultByOpenDTO.setHmShopownResultDTOS(hmShopownResultDTOList);
                    hmMarketResultByOpenDTO.setMartketId(hmShopownResultDTO.getMartketId());
                    hmMarketResultByOpenDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByOpenDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByOpenDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByOpenDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());
                    marketResultByOpenDTOList.add(hmMarketResultByOpenDTO);
                }else{
                    if(marketResultByCloseDTOList==null){
                        marketResultByCloseDTOList = new ArrayList<>();
                    }
                    //商家信息
                    List<HmShopownResultDTO> hmShopownResultDTOList =new ArrayList<>();
                    //打烊
                    HmMarketResultByCloseDTO hmMarketResultByCloseDTO = new HmMarketResultByCloseDTO();
                    HmShopownResultDTO hmShopownResult = new HmShopownResultDTO();
                    BeanUtils.copyProperties(shopownResultDTO,hmShopownResult);
                    hmShopownResultDTOList.add(hmShopownResult);
                    hmMarketResultByCloseDTO.setHmShopownResultDTOS(hmShopownResultDTOList);
                    hmMarketResultByCloseDTO.setHmShopownResultDTOS(hmShopownResultDTOList);
                    hmMarketResultByCloseDTO.setMartketId(hmShopownResultDTO.getMartketId());
                    hmMarketResultByCloseDTO.setMarketName(hmShopownResultDTO.getMarketName());
                    hmMarketResultByCloseDTO.setRule(hmShopownResultDTO.getRule());
                    hmMarketResultByCloseDTO.setStatus(hmShopownResultDTO.getStatus());
                    hmMarketResultByCloseDTO.setHmMarketActivityResultDTO(hmShopownResultDTO.getHmMarketActivityResultDTO());
                    marketResultByCloseDTOList.add(hmMarketResultByCloseDTO);
                }
            }
            //菜场活动信息
            hmMarketActivityResultDTO = hmShopownResultDTO.getHmMarketActivityResultDTO();
            if (null != hmMarketActivityResultDTO) {
                String[] split = hmMarketActivityResultDTO.getRule().split("-");
                if (FinalDatas.ZERO.equals(hmMarketActivityResultDTO.getType())) {
                    stringBuffer = new StringBuffer();
                    stringBuffer.append("首单购买满").append(split[0]).append("减").append(split[1]);
                    hmShopownResultDTO.setRule(stringBuffer.toString());
                }
                if (FinalDatas.ONE.equals(hmMarketActivityResultDTO.getType())) {
                    stringBuffer = new StringBuffer();
                    //活动描述
                    String rule = hmMarketActivityResultDTO.getRule();
                    stringBuffer.append("首单购买打").append(decimalFormat.format(Double.parseDouble(rule) * 10)).append("折");
                    hmShopownResultDTO.setRule(stringBuffer.toString());
                }
                if (!StringUtils.isBlank(hmMarketActivityResultDTO.getRule())) {
                    if (BigDecimal.ZERO.compareTo(disPrice) != 0) {
                        if (disPrice.compareTo(new BigDecimal(split[0])) == 1) {
                            totalDisPrice = disPrice.subtract(new BigDecimal(split[1]));
                        }
                    } else {
                        if (totalNumber.compareTo(new BigDecimal(split[0])) == 1) {
                            totalDisPrice = totalNumber.subtract(new BigDecimal(split[1]));
                        }
                    }
                }
                hmShopownResultDTO.setStatus(hmMarketActivityResultDTO.getStatus());
                hmShopownResultDTO.setTotalPrice(String.valueOf(totalDisPrice.setScale(2)));
            }
        }
        hmMarketResultListDTO.setHmMarketResultByAdvance(hmMarketResultByAdvenceDTOList);
        hmMarketResultListDTO.setHmMarketResultByClose(marketResultByCloseDTOList);
        hmMarketResultListDTO.setHmMarketResultByOpen(marketResultByOpenDTOList);

        resultDTO.setFlag(true);
        resultDTO.setData(hmMarketResultListDTO);
        resultDTO.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
        resultDTO.setStatusCode(CommonMessageEnum.SUCCESS.getCode());
        return resultDTO;
    }


}
