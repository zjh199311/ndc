package com.zhongjian.service.order.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.hm.address.HmAddressBean;
import com.zhongjian.dao.entity.hm.shopown.HmShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.framework.inf.HmDAO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;
import com.zhongjian.dto.order.address.result.OrderPreviewAddressResultDTO;
import com.zhongjian.dto.order.order.query.OrderStatusQueryDTO;
import com.zhongjian.dto.order.order.result.OrderPreviewResultDTO;
import com.zhongjian.dto.order.order.result.OrderPreviewShopownResultDTO;
import com.zhongjian.service.order.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl extends HmBaseService<HmShopownBean, Integer>
        implements OrderService {

    private HmDAO<HmAddressBean, Integer> hmAddressDAO;

    @Resource
    public void setHmAddressDAO(HmDAO<HmAddressBean, Integer> hmAddressDAO) {
        this.hmAddressDAO = hmAddressDAO;
        this.hmAddressDAO.setPerfix(HmAddressBean.class.getName());
    }

    @Override
    public ResultDTO<Object> previewOrder(Integer uid,
                                          Integer[] sids, Integer isAppointment, String type, String extra,
                                          String isSelfMention) {
        OrderPreviewResultDTO orderResultDTO = new OrderPreviewResultDTO();
        // 根据isAppointment 判断是否为预约单。 是，配送时间为第二天8.48分。 否 配送时间为当前实际+48分
        if (isAppointment == 1) {

            orderResultDTO.setDeliveryTime("");
        }
        orderResultDTO.setDeliveryTime("配送时间");

        // 获取默认收货地址 取到就存。没取到就不存
        OrderPreviewAddressResultDTO addressResultDTO = new OrderPreviewAddressResultDTO();
        orderResultDTO.setAddressResultDTO(addressResultDTO);

        // 获取所有待支付的商铺信息存进去。中间包含价格计算规则
        List<OrderPreviewShopownResultDTO> shopownResultDTOs = new ArrayList<OrderPreviewShopownResultDTO>();
        // 这里的逻辑可能会比较多。
        // 先根据店铺id查到所有要查出的店铺。
        // 获取所有店铺的活动
        // 找出每个店铺的所有已选商品
        // 计算每个店铺的总金额，然后再与活动规则做计算。
        // 得到每个店铺应付，实付，减免，存储到集合中
        orderResultDTO.setShopownResultDTOs(shopownResultDTOs);

        // 存储活动规则。如果是优惠卷还需要查出来存进去 是积分就根据积分规则存，是菜场优惠就根据菜场活动存储
        // 优惠类型
        orderResultDTO.setDiscountsType(0);
        // 优惠金额
        orderResultDTO.setDiscountsPrice("0");

        // 存储商品应付总金额。为上面所有店铺实付金额的总和
        orderResultDTO.setTotalPrice("0");

        // 存储配送类型以及配送费 如果是配送需要判断是否会员
        orderResultDTO.setDeliveryType(0);
        orderResultDTO.setDeliveryPrice("6");

        // 存储会员类型以及开通后减免金额
        orderResultDTO.setVip(0);
        orderResultDTO.setVipDiscountsPrice("13.3");

        // 存储实际支付金额
        orderResultDTO.setPayPrice("133");
        return ResultUtil.getSuccess(orderResultDTO);
    }

    @Override
    public ResultDTO<Object> judgeHmShopownStatus(OrderStatusQueryDTO orderStatusQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        if (null == orderStatusQueryDTO.getPids()
                || 0 == orderStatusQueryDTO.getPids().size()) {
            return ResultUtil.getFail(CommonMessageEnum.PID_IS_NULL);
        }
        if (null == orderStatusQueryDTO.getStatus()) {
            return ResultUtil.getFail(CommonMessageEnum.STATUS_IS_NULL);
        }
        List<HmShopownBean> hmShopownBeans = this.dao.executeListMethod(
                orderStatusQueryDTO, "selectHmShopownStatusByPids",
                HmShopownBean.class);
        // 默认返回状态匹配
        resultDTO.setFlag(true);
        resultDTO.setData(true);
        for (HmShopownBean hmShopownBean : hmShopownBeans) {
            // 如果商户状态与传入状态不匹配，返回false
            if (!orderStatusQueryDTO.getStatus().equals(
                    hmShopownBean.getStatus())) {
                resultDTO.setData(false);
                break;
            }
            // 如果店铺状态为预约中，但是该店铺没有开启预约 返回false
            if (2 == hmShopownBean.getStatus()
                    && 0 == hmShopownBean.getIsAppointment()) {
                resultDTO.setData(false);
                break;
            }
            //如果店铺状态为打烊或开张并且店铺为开启预约。返回false
            if ((1 == hmShopownBean.getStatus() || 0 == hmShopownBean
                    .getStatus()) && 1 == hmShopownBean.getIsAppointment()) {
                resultDTO.setData(false);
                break;
            }
        }
        return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
    }

    @Override
    public OrderAddressResultDTO previewOrderAddress(OrderAddressQueryDTO orderAddressQueryDTO) {
        /**
         * 要是传来的id为0.则根据uid去查询数据库并根据status为1的默认地址返回.limit=1
         * 要是传来的id不为0,则根据id去查询在返回
         */
        if (FinalDatas.ZERO == orderAddressQueryDTO.getId()) {
            OrderAddressResultDTO orderAddressResultDTO=   this.hmAddressDAO.executeSelectOneMethod(orderAddressQueryDTO.getUid(), "findAddressByUid", OrderAddressResultDTO.class);
            return orderAddressResultDTO;
        } else {
            OrderAddressResultDTO findAddressByid = this.hmAddressDAO.executeSelectOneMethod(orderAddressQueryDTO.getId(), "findAddressByid", OrderAddressResultDTO.class);
            return findAddressByid;
        }
    }

    @Override
    public String previewOrderTime(Integer isAppointment) {
        return null;
    }


}
