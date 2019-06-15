package com.zhongjian.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.common.constant.enums.CvorderEnum;
import com.zhongjian.dao.entity.order.address.OrderAddressBean;
import com.zhongjian.dao.entity.order.cvstore.OrderCvOrderBean;
import com.zhongjian.dao.entity.order.cvstore.OrderCvUserOrderBean;
import com.zhongjian.dao.entity.order.shopown.OrderShopownBean;
import com.zhongjian.dao.entity.order.user.OrderUserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.cart.address.result.OrderAddressResultDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.order.order.query.OrderQueryDTO;
import com.zhongjian.dto.order.order.result.OrderCvOrderResultDTO;
import com.zhongjian.dto.order.order.result.OrderItemResultDTO;
import com.zhongjian.dto.order.order.result.OrderListResultDTO;
import com.zhongjian.service.order.OrderCvstoreDetailsSerivce;
import com.zhongjian.util.DateUtil;
import com.zhongjian.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: ldd
 */
@Service
public class OrderCvstoreDetailsSerivceImpl extends HmBaseService<OrderCvUserOrderBean, Integer> implements OrderCvstoreDetailsSerivce {

    private HmDAO<OrderUserBean, Integer> userBean;

    private HmDAO<OrderAddressBean, Integer> orderAddressBean;

    private HmDAO<OrderShopownBean, Integer> orderShopownBean;

    private HmDAO<OrderCvOrderBean, Integer> orderCvOrderBean;


    @Resource
    public void setOrderShopownBean(HmDAO<OrderShopownBean, Integer> orderShopownBean) {
        this.orderShopownBean = orderShopownBean;
        this.orderShopownBean.setPerfix(OrderShopownBean.class.getName());
    }

    @Resource
    public void setUserBean(HmDAO<OrderUserBean, Integer> userBean) {
        this.userBean = userBean;
        this.userBean.setPerfix(OrderUserBean.class.getName());
    }


    @Resource
    public void setOrderAddressBean(HmDAO<OrderAddressBean, Integer> orderAddressBean) {
        this.orderAddressBean = orderAddressBean;
        this.orderAddressBean.setPerfix(OrderAddressBean.class.getName());
    }

    @Resource
    public void setOrderCvOrderBean(HmDAO<OrderCvOrderBean, Integer> orderCvOrderBean) {
        this.orderCvOrderBean = orderCvOrderBean;
        this.orderCvOrderBean.setPerfix(OrderCvOrderBean.class.getName());
    }

    @Override
    public ResultDTO<Object> orderDetails(OrderQueryDTO orderQueryDTO) {
        if (null == orderQueryDTO.getRoid()) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        if (null == orderQueryDTO.getUid()) {
            return ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }
        //商品总价
        BigDecimal totalPrice = BigDecimal.ZERO;

        //判断该用户是否为vip会员
        OrderUserBean orderUserBean = this.userBean.selectByPrimaryKey(orderQueryDTO.getUid());

        OrderItemResultDTO cvOrderDetail = this.dao.executeSelectOneMethod(orderQueryDTO, "CvOrderDetail", OrderItemResultDTO.class);
        if (null == cvOrderDetail) {
            LogUtil.info("订单为空", "cvOrderDetail" + cvOrderDetail);
            return ResultUtil.getSuccess(null);
        } else {
            //创建时间转换
            long createTime = cvOrderDetail.getCtime() * 1000L;
            cvOrderDetail.setCreateTime(DateUtil.lastDayTime.format(createTime));
            cvOrderDetail.setCtime(null);

            //积分优惠
            if (null != cvOrderDetail.getIntegralPrice() && new BigDecimal(cvOrderDetail.getIntegralPrice()).compareTo(BigDecimal.ZERO) != 0) {
                cvOrderDetail.setIntegralPrice("-¥" + new BigDecimal(cvOrderDetail.getIntegralPrice()).divide(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP)).intValue());
            } else {
                cvOrderDetail.setIntegralPrice(null);
            }
            //优惠券优惠
            if (null != cvOrderDetail.getCouponPrice()) {
                cvOrderDetail.setCouponPrice("-¥" + new BigDecimal(cvOrderDetail.getCouponPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
            } else {
                cvOrderDetail.setCouponPrice(null);
            }
            //根据uoid查询订单明细(一对一)
            OrderCvOrderResultDTO findCvOrderByUoid = this.orderCvOrderBean.executeSelectOneMethod(cvOrderDetail.getId(), "findCvOrderByUoid", OrderCvOrderResultDTO.class);

            if (null == findCvOrderByUoid) {
                LogUtil.info("根据订单id查询为空", "findCvOrderByUoid" + findCvOrderByUoid);
            } else {
                cvOrderDetail.setAddressId(findCvOrderByUoid.getAddressId());
                //配送费
                if (null != findCvOrderByUoid.getDeliverFee() && new BigDecimal(findCvOrderByUoid.getDeliverFee()).compareTo(BigDecimal.ZERO) != 0) {
                    cvOrderDetail.setDistributionFee("¥" + new BigDecimal(findCvOrderByUoid.getDeliverFee()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
                } else {
                    cvOrderDetail.setDistributionFee(null);
                }
                //装换时间戳(预约时间)
                if (null != findCvOrderByUoid.getTime()) {
                    long serverTime = findCvOrderByUoid.getTime() * 1000L;
                    String format = DateUtil.lastDayTime.format(serverTime);
                    try {
                        //判断天是否与当天一样如果不一样则加上字符串明天.如果一样则不变
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(DateUtil.lastDayTime.parse(format));
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        Calendar calendayNow = Calendar.getInstance();
                        int nowDay = calendayNow.get(Calendar.DAY_OF_MONTH);
                        if (day == nowDay) {
                            cvOrderDetail.setServerTime(format);
                        } else {
                            cvOrderDetail.setServerTime("明天" + format);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }


            //首先判断骑手状态和支付状态是否为0如果是则显示会员信息如果否则不显示
            if (FinalDatas.ZERO == findCvOrderByUoid.getPayStatus() && FinalDatas.ZERO == findCvOrderByUoid.getRiderStatus()) {
                cvOrderDetail.setRiderSn(null);
                //判断是否为会员
                if (FinalDatas.ONE == orderUserBean.getVipStatus()) {
                    cvOrderDetail.setIsMember(1);
                } else {
                    cvOrderDetail.setIsMember(0);
                    cvOrderDetail.setSelfLifting("会员用户专享");
                    cvOrderDetail.setMemberContent("会员用户专享");
                }
                //判断是否为自提
                if (FinalDatas.ZERO == findCvOrderByUoid.getPayStatus() && FinalDatas.THREE == findCvOrderByUoid.getRiderStatus()) {
                    cvOrderDetail.setStatus(3);
                }
            } else if (FinalDatas.ONE == cvOrderDetail.getPayStatus()) {

                switch (findCvOrderByUoid.getRiderStatus()) {
                    case 0:
                        cvOrderDetail.setStatusMsg(CvorderEnum.ORDER_DISTRIBUTION.getMsg());
                        cvOrderDetail.setStatus(0);
                        break;
                    case 1:
                        cvOrderDetail.setStatusMsg(CvorderEnum.BEING_DISPATCHED.getMsg());
                        cvOrderDetail.setStatus(1);
                        break;
                    case 2:
                        cvOrderDetail.setStatusMsg(CvorderEnum.COMPLETED.getMsg());
                        cvOrderDetail.setStatus(2);
                        break;
                    case 3:
                        cvOrderDetail.setStatusMsg(CvorderEnum.SELF_MENTION.getMsg());
                        cvOrderDetail.setStatus(3);
                        break;
                    case 4:
                        cvOrderDetail.setStatusMsg(CvorderEnum.EVALUATION_COMPLETED.getMsg());
                        cvOrderDetail.setStatus(4);
                        break;
                    default:
                }
            }

            //家庭住址
            if (null != findCvOrderByUoid.getAddressId()) {
                OrderAddressResultDTO selectAddressById = this.orderAddressBean.executeSelectOneMethod(findCvOrderByUoid.getAddressId(), "selectAddressById", OrderAddressResultDTO.class);
                cvOrderDetail.setAddress(selectAddressById);
            }

            //订单
            if (null != cvOrderDetail.getId()) {
                List<OrderListResultDTO> findShopownByOid = this.orderShopownBean.executeListMethod(cvOrderDetail.getId(), "findShopownByUoid", OrderListResultDTO.class);
                if(!CollectionUtils.isEmpty(findShopownByOid)){
                    for (OrderListResultDTO orderListResultDTO : findShopownByOid) {
                        //商品订单原价，当实际价格与原价相同，显示为空
                        if (new BigDecimal(orderListResultDTO.getOrderTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(orderListResultDTO.getOrderPayment()).setScale(2, BigDecimal.ROUND_HALF_UP)) == 0) {
                            orderListResultDTO.setOrderTotal(null);
                        } else {
                            orderListResultDTO.setOrderTotal("¥" + new BigDecimal(orderListResultDTO.getOrderTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
                        }

                        totalPrice = totalPrice.add(new BigDecimal(orderListResultDTO.getOrderPayment()));

                        orderListResultDTO.setOrderPayment("¥" + new BigDecimal(orderListResultDTO.getOrderPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
                    }
                    cvOrderDetail.setOrderList(findShopownByOid);
                }
            }
            cvOrderDetail.setFoodPrice("¥" + new BigDecimal(String.valueOf(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
            cvOrderDetail.setTotalPrice("¥" + new BigDecimal(cvOrderDetail.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
        }

        return ResultUtil.getSuccess(cvOrderDetail);
    }
}