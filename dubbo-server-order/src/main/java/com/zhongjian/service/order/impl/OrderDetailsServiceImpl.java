package com.zhongjian.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.common.constant.enums.CvorderEnum;
import com.zhongjian.dao.entity.order.cart.OrderCartBean;
import com.zhongjian.dao.entity.order.cvorder.OrderCvOrderDetailBean;
import com.zhongjian.dao.entity.order.cvstore.OrderCvOrderBean;
import com.zhongjian.dao.entity.order.cvstore.OrderCvUserOrderBean;
import com.zhongjian.dao.entity.order.goods.OrderGoodsBean;
import com.zhongjian.dao.entity.order.rider.OrderRiderOrderBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.Page;
import com.zhongjian.dto.cart.basket.result.CartBasketResultDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.order.order.query.OrderQueryDTO;
import com.zhongjian.dto.order.order.result.OrderDetailsResultDTO;
import com.zhongjian.service.order.OrderDetailsService;
import com.zhongjian.util.DateUtil;
import com.zhongjian.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ldd
 */
@Service("orderDetailsService")
public class OrderDetailsServiceImpl extends HmBaseService<OrderCvUserOrderBean, Integer> implements OrderDetailsService {

    private HmDAO<OrderCvOrderBean, Integer> orderCvOrderBean;

    private HmDAO<OrderRiderOrderBean, Integer> orderRiderOrderBean;

    private HmDAO<OrderCvOrderDetailBean, Integer> orderDetailBean;

    private HmDAO<OrderCartBean, Integer> orderCartBean;

    private HmDAO<OrderGoodsBean, Integer> orderGoodsBean;

    @Resource
    private void setOrderGoodsBean(HmDAO<OrderGoodsBean, Integer> orderGoodsBean) {
        this.orderGoodsBean = orderGoodsBean;
        orderGoodsBean.setPerfix(OrderGoodsBean.class.getName());
    }

    @Resource
    private void setOrderDetailBean(HmDAO<OrderCvOrderDetailBean, Integer> orderDetailBean) {
        this.orderDetailBean = orderDetailBean;
        orderDetailBean.setPerfix(OrderCvOrderDetailBean.class.getName());
    }

    @Resource
    private void setOrderCartBean(HmDAO<OrderCartBean, Integer> orderCartBean) {
        this.orderCartBean = orderCartBean;
        orderCartBean.setPerfix(OrderCartBean.class.getName());
    }

    @Resource
    private void setOrderRiderOrderBean(HmDAO<OrderRiderOrderBean, Integer> orderRiderOrderBean) {
        this.orderRiderOrderBean = orderRiderOrderBean;
        orderRiderOrderBean.setPerfix(OrderRiderOrderBean.class.getName());
    }

    @Resource
    public void setOrderCvOrderBean(HmDAO<OrderCvOrderBean, Integer> orderCvOrderBean) {
        this.orderCvOrderBean = orderCvOrderBean;
        orderCvOrderBean.setPerfix(OrderCvOrderBean.class.getName());
    }


    @Override
    public ResultDTO<Object> queryList(OrderQueryDTO orderQueryDTO, Page page) {
        if (null == orderQueryDTO.getUid()) {
            ResultUtil.getFail(CommonMessageEnum.PARAM_LOST);
        }

        List<CartBasketResultDTO> list = null;

        List<OrderDetailsResultDTO> incomplete = new ArrayList<>();

        List<OrderDetailsResultDTO> complete = new ArrayList<>();

        //获取便利店和菜场的订单
        List<OrderDetailsResultDTO> orderDetailsResultDTOS = this.dao.executeListMethod(orderQueryDTO.getUid(), "queryList", page, "queryListCount", OrderDetailsResultDTO.class);
        for (OrderDetailsResultDTO orderDetailsResultDTO : orderDetailsResultDTOS) {

            if (FinalDatas.ZERO == orderDetailsResultDTOS.size()) {
                orderDetailsResultDTO.setShowMore(0);
            }else{
                orderDetailsResultDTO.setShowMore(1);
            }

            //转换时间格式
            orderDetailsResultDTO.setTime(DateUtil.lastDayTime.format(orderDetailsResultDTO.getCtime()));

            //如果type状态为1则为便利店订单. 为0则为菜场订单
            if (FinalDatas.ONE == orderDetailsResultDTO.getType()) {
                list = this.orderDetailBean.executeListMethod(orderDetailsResultDTO.getRoid(), "findCvOrderByOid", CartBasketResultDTO.class);
                orderDetailsResultDTO.setList(list);
            } else {
                list = this.orderCartBean.executeListMethod(orderDetailsResultDTO.getRoid(), "findCartOrderByOid", CartBasketResultDTO.class);
                for (CartBasketResultDTO cartBasketResultDTO : list) {
                    if (StringUtil.isBlank(cartBasketResultDTO.getGname())) {
                        OrderGoodsBean orderGoodsBean = this.orderGoodsBean.selectByPrimaryKey(cartBasketResultDTO.getId());
                        cartBasketResultDTO.setGname(orderGoodsBean.getGname());
                    }
                }
                orderDetailsResultDTO.setList(list);
            }
            //状态描述
            if (FinalDatas.ONE == orderDetailsResultDTO.getPayStatus()) {
                switch (orderDetailsResultDTO.getRiderStatus()) {
                    case 0:
                        orderDetailsResultDTO.setStatusContent(CvorderEnum.ORDER_DISTRIBUTION.getMsg());
                        orderDetailsResultDTO.setButtonContent("");
                        orderDetailsResultDTO.setStatus(0);
                        break;
                    case 1:
                        if (FinalDatas.ONE == orderDetailsResultDTO.getType()) {
                            orderDetailsResultDTO.setButtonContent("");
                        } else {
                            orderDetailsResultDTO.setButtonContent(CvorderEnum.VIEW_RIDERS.getMsg());
                        }
                        orderDetailsResultDTO.setStatusContent(CvorderEnum.BEING_DISPATCHED.getMsg());
                        orderDetailsResultDTO.setStatus(1);
                        break;
                    case 2:
                        orderDetailsResultDTO.setStatusContent(CvorderEnum.COMPLETED.getMsg());
                        orderDetailsResultDTO.setButtonContent(CvorderEnum.EVALUATE_IT.getMsg());
                        orderDetailsResultDTO.setStatus(2);
                        break;
                    case 3:
                        orderDetailsResultDTO.setStatusContent(CvorderEnum.SELF_MENTION.getMsg());
                        orderDetailsResultDTO.setButtonContent("");
                        orderDetailsResultDTO.setStatus(3);
                        break;
                    case 4:
                        orderDetailsResultDTO.setStatusContent(CvorderEnum.EVALUATION_COMPLETED.getMsg());
                        orderDetailsResultDTO.setButtonContent("");
                        orderDetailsResultDTO.setStatus(4);
                        break;
                }
                if (FinalDatas.ONE == orderDetailsResultDTO.getIsAppointment() && FinalDatas.ZERO == orderDetailsResultDTO.getRiderStatus()) {
                    orderDetailsResultDTO.setStatusContent(CvorderEnum.ORDER_RESERVATION.getMsg());
                    orderDetailsResultDTO.setButtonContent("");
                    orderDetailsResultDTO.setStatus(5);
                }
            } else {
                orderDetailsResultDTO.setStatusContent(CvorderEnum.TO_BE_PAID.getMsg());
                orderDetailsResultDTO.setButtonContent(CvorderEnum.IMMEDIATE_PAYMENT.getMsg());
                orderDetailsResultDTO.setStatus(6);
            }
            //未完成订单当前端传0
            if (null != orderQueryDTO.getStatus() && FinalDatas.ZERO == orderQueryDTO.getStatus()) {
                if (FinalDatas.ONE == orderDetailsResultDTO.getPayStatus()) {
                    if (FinalDatas.ZERO == orderDetailsResultDTO.getRiderStatus() || FinalDatas.ONE == orderDetailsResultDTO.getRiderStatus() || FinalDatas.THREE == orderDetailsResultDTO.getRiderStatus()) {
                        incomplete.add(orderDetailsResultDTO);
                    }
                }
            }
            if (null != orderQueryDTO.getStatus() && FinalDatas.ONE == orderQueryDTO.getStatus()) {
                if (FinalDatas.ONE == orderDetailsResultDTO.getPayStatus()) {
                    if (FinalDatas.TWO == orderDetailsResultDTO.getRiderStatus() || FinalDatas.FOUR == orderDetailsResultDTO.getRiderStatus()) {
                        complete.add(orderDetailsResultDTO);
                    }
                }
            }
            orderDetailsResultDTO.setPayStatus(null);
            orderDetailsResultDTO.setRiderStatus(null);
        }
        if (FinalDatas.ZERO == orderQueryDTO.getStatus() || !CollectionUtils.isEmpty(incomplete)) {
            return ResultUtil.getSuccess(incomplete);
        }

        if (FinalDatas.ONE == orderQueryDTO.getStatus() || !CollectionUtils.isEmpty(complete)) {
            return ResultUtil.getSuccess(complete);
        }

        return ResultUtil.getSuccess(orderDetailsResultDTOS);
    }

}
