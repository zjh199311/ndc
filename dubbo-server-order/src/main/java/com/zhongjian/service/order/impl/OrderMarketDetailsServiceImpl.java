package com.zhongjian.service.order.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.common.constant.enums.CvorderEnum;
import com.zhongjian.dao.entity.order.address.OrderAddressBean;
import com.zhongjian.dao.entity.order.cart.OrderCartBean;
import com.zhongjian.dao.entity.order.goods.OrderGoodsBean;
import com.zhongjian.dao.entity.order.order.OrderBean;
import com.zhongjian.dao.entity.order.rider.OrderRiderUserBean;
import com.zhongjian.dao.entity.order.shopown.OrderShopownBean;
import com.zhongjian.dao.entity.order.user.OrderUserBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.cart.address.result.OrderAddressResultDTO;
import com.zhongjian.dto.cart.basket.result.CartBasketResultDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.order.order.query.OrderQueryDTO;
import com.zhongjian.dto.order.order.result.OrderItemResultDTO;
import com.zhongjian.dto.order.order.result.OrderListResultDTO;
import com.zhongjian.service.order.OrderMarketDetailsService;
import com.zhongjian.util.DateUtil;
import com.zhongjian.util.LogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: ldd
 */
@Service
public class OrderMarketDetailsServiceImpl extends HmBaseService<OrderBean, Integer> implements OrderMarketDetailsService {

    private HmDAO<OrderUserBean, Integer> userBean;

    private HmDAO<OrderAddressBean, Integer> orderAddressBean;

    private HmDAO<OrderShopownBean, Integer> orderShopownBean;

    private HmDAO<OrderRiderUserBean, Integer> orderRiderUserBean;

    private HmDAO<OrderCartBean, Integer> orderCartBean;

    private HmDAO<OrderGoodsBean, Integer> orderGoodsBean;

    @Resource
    public void setOrderGoodsBean(HmDAO<OrderGoodsBean, Integer> orderGoodsBean) {
        this.orderGoodsBean = orderGoodsBean;
        this.orderGoodsBean.setPerfix(OrderGoodsBean.class.getName());
    }

    @Resource
    public void setOrderCartBean(HmDAO<OrderCartBean, Integer> orderCartBean) {
        this.orderCartBean = orderCartBean;
        this.orderCartBean.setPerfix(OrderCartBean.class.getName());
    }


    @Resource
    public void setOrderRiderUserBean(HmDAO<OrderRiderUserBean, Integer> orderRiderUserBean) {
        this.orderRiderUserBean = orderRiderUserBean;
        this.orderRiderUserBean.setPerfix(OrderRiderUserBean.class.getName());
    }


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

        OrderItemResultDTO findOrderItem = this.dao.executeSelectOneMethod(orderQueryDTO, "findOrderItem", OrderItemResultDTO.class);
        if (null == findOrderItem) {
            LogUtil.info("订单为空", "findOrderItem" + findOrderItem);
            return ResultUtil.getSuccess(null);
        } else {

            //装换时间戳
            if (null != findOrderItem.getPtime()) {
                long createTime = findOrderItem.getPtime() * 1000L;
                findOrderItem.setPayTime(DateUtil.datetime_format.format(createTime));
                findOrderItem.setPtime(null);
            }
            //装换时间戳
            if (null != findOrderItem.getCtime()) {
                long createTime = findOrderItem.getCtime() * 1000L;
                findOrderItem.setCreateTime(DateUtil.datetime_format.format(createTime));
                findOrderItem.setCtime(null);
            }

            //积分优惠
            if (null != findOrderItem.getIntegralPrice() && new BigDecimal(findOrderItem.getIntegralPrice()).compareTo(BigDecimal.ZERO) != 0) {
                findOrderItem.setIntegralPrice("-¥" + new BigDecimal(findOrderItem.getIntegralPrice()).divide(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP)).intValue());
            } else {
                findOrderItem.setIntegralPrice(null);
            }
            //菜场优惠
            if (null != findOrderItem.getMarketActivityPrice() && new BigDecimal(findOrderItem.getMarketActivityPrice()).compareTo(BigDecimal.ZERO) != 0) {
                findOrderItem.setMarketActivityPrice("-¥" + new BigDecimal(findOrderItem.getMarketActivityPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
            } else {
                findOrderItem.setMarketActivityPrice(null);
            }
            //优惠券优惠
            if (null != findOrderItem.getCouponPrice() && new BigDecimal(findOrderItem.getCouponPrice()).compareTo(BigDecimal.ZERO) != 0) {
                findOrderItem.setCouponPrice("-¥" + new BigDecimal(findOrderItem.getCouponPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
            } else {
                findOrderItem.setCouponPrice(null);
            }
            //会员折扣
            if (null != findOrderItem.getDelMemberPrice() && new BigDecimal(findOrderItem.getDelMemberPrice()).compareTo(BigDecimal.ZERO) != 0) {
                findOrderItem.setDelMemberPrice("-¥" + new BigDecimal(findOrderItem.getDelMemberPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
            } else {
                findOrderItem.setDelMemberPrice(null);
            }
            //配送费
            if (null != findOrderItem.getDistributionFee() && new BigDecimal(findOrderItem.getDistributionFee()).compareTo(BigDecimal.ZERO) != 0) {
                findOrderItem.setDistributionFee("¥" + new BigDecimal(findOrderItem.getDistributionFee()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
            } else {
                findOrderItem.setDistributionFee(null);
            }
            //首先判断骑手状态和支付状态是否为0如果是则显示会员信息如果否则不显示
            if ((FinalDatas.ZERO == findOrderItem.getPayStatus() && FinalDatas.ZERO == findOrderItem.getRiderStatus()) ||
                    (FinalDatas.ZERO == findOrderItem.getPayStatus() && FinalDatas.THREE == findOrderItem.getRiderStatus())) {
                //判断是否为会员
                if (FinalDatas.ONE == orderUserBean.getVipStatus()) {
                    findOrderItem.setIsMember(1);
                } else {
                    findOrderItem.setIsMember(0);
                    findOrderItem.setSelfLifting("会员用户专享");
                    findOrderItem.setMemberContent("会员用户专享");
                }
                //判断是否为自提
                if (FinalDatas.ZERO == findOrderItem.getPayStatus() && FinalDatas.THREE == findOrderItem.getRiderStatus()) {
                    findOrderItem.setStatus(3);
                }
            }
            //装换时间戳(预约时间)
            long serverTime = findOrderItem.getTime() * 1000L;

            if (FinalDatas.ONE == findOrderItem.getPayStatus()) {
                switch (findOrderItem.getRiderStatus()) {
                    case 0:
                        findOrderItem.setStatusMsg(CvorderEnum.ORDER_DISTRIBUTION.getMsg());
                        findOrderItem.setStatus(0);
                        break;
                    case 1:
                        findOrderItem.setStatusMsg(CvorderEnum.BEING_DISPATCHED.getMsg());
                        findOrderItem.setStatus(1);
                        break;
                    case 2:
                        findOrderItem.setStatusMsg(CvorderEnum.COMPLETED.getMsg());
                        findOrderItem.setStatus(2);
                        break;
                    case 3:
                        findOrderItem.setStatusMsg(CvorderEnum.SELF_MENTION.getMsg());
                        findOrderItem.setStatus(3);
                        break;
                    case 4:
                        findOrderItem.setStatusMsg(CvorderEnum.EVALUATION_COMPLETED.getMsg());
                        findOrderItem.setStatus(4);
                        break;
                    default:
                }
                //如果是完成订单的话显示送达时间其他则显示预计送达时间
                if (FinalDatas.TWO == findOrderItem.getStatus() || FinalDatas.FOUR == findOrderItem.getStatus()) {
                    //装换时间戳
                    if (null != findOrderItem.getFinishTime()) {
                        long finishTime = findOrderItem.getFinishTime() * 1000L;
                        findOrderItem.setServiceTime(DateUtil.datetime_format.format(finishTime));
                        findOrderItem.setFinishTime(null);
                        findOrderItem.setTime(null);
                    }
                } else {
                    String format = DateUtil.datetime_format.format(serverTime);
                    findOrderItem.setServerTime(format);
                    findOrderItem.setTime(null);
                    findOrderItem.setFinishTime(null);
                }
            } else {
                String format = DateUtil.lastDayTime.format(serverTime);
                try {
                    //判断天是否与当天一样如果不一样则加上字符串明天.如果一样则不变
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateUtil.lastDayTime.parse(format));
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    Calendar calendayNow = Calendar.getInstance();
                    int nowDay = calendayNow.get(Calendar.DAY_OF_MONTH);
                    if (day == nowDay) {
                        findOrderItem.setServerTime(format);
                    } else {
                        findOrderItem.setServerTime("明天" + format);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                findOrderItem.setTime(null);
            }
            //骑手
            if (FinalDatas.ONE == findOrderItem.getPayStatus()) {
                if (null != findOrderItem.getRid()) {
                    OrderRiderUserBean orderRiderUserBean = this.orderRiderUserBean.selectByPrimaryKey(findOrderItem.getRid());
                    findOrderItem.setRiderUserName(orderRiderUserBean.getName());
                    findOrderItem.setRiderPhone(orderRiderUserBean.getPhone());
                }
            }


            //家庭住址
            if (null != findOrderItem.getAddressId()) {
                OrderAddressResultDTO selectAddressById = this.orderAddressBean.executeSelectOneMethod(findOrderItem.getAddressId(), "selectAddressById", OrderAddressResultDTO.class);
                findOrderItem.setAddress(selectAddressById);
            }
            //订单
            if (null != findOrderItem.getId()) {
                List<OrderListResultDTO> findShopownByOid = this.orderShopownBean.executeListMethod(findOrderItem.getId(), "findShopownByOid", OrderListResultDTO.class);
                for (OrderListResultDTO orderListResultDTO : findShopownByOid) {

                    //商品订单原价，当实际价格与原价相同，显示为空
                    if (new BigDecimal(orderListResultDTO.getOrderTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(orderListResultDTO.getOrderPayment()).setScale(2, BigDecimal.ROUND_HALF_UP)) == 0) {
                        orderListResultDTO.setOrderTotal(null);
                    } else {
                        orderListResultDTO.setOrderTotal("¥" + new BigDecimal(orderListResultDTO.getOrderTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
                    }
                    totalPrice = totalPrice.add(new BigDecimal(orderListResultDTO.getOrderPayment()));
                    orderListResultDTO.setOrderPayment("¥" + new BigDecimal(orderListResultDTO.getOrderPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());

                    //订单显示的食品信息
                    List<CartBasketResultDTO> findGidByOid = this.orderCartBean.executeListMethod(orderListResultDTO.getOid(), "findGidByOid", CartBasketResultDTO.class);
                    //根据gid获取食品名称
                    List<CartBasketResultDTO> list = new ArrayList<CartBasketResultDTO>();
                    for (CartBasketResultDTO cartBasketResultBean : findGidByOid) {
                        CartBasketResultDTO cartBasketResultDTO = new CartBasketResultDTO();
                        OrderGoodsBean orderGoodsBean = this.orderGoodsBean.selectByPrimaryKey(cartBasketResultBean.getGid());
                        cartBasketResultDTO.setGname(orderGoodsBean.getGname() + "(" + cartBasketResultBean.getAmount() + orderGoodsBean.getUnit() + ")");
                        cartBasketResultDTO.setPrice("¥" + orderGoodsBean.getPrice().toString());
                        list.add(cartBasketResultDTO);
                    }
                    orderListResultDTO.setCartList(list);
                    orderListResultDTO.setAmountMsg("共" + findGidByOid.size() + "种");
                }

                findOrderItem.setStoreList(findShopownByOid);

            }
            findOrderItem.setFoodPrice("¥" + new BigDecimal(String.valueOf(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
            findOrderItem.setTotalPrice("¥" + new BigDecimal(findOrderItem.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
        }

        return ResultUtil.getSuccess(findOrderItem);
    }
}
