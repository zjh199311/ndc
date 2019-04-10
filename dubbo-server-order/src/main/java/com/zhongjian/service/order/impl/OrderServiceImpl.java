package com.zhongjian.service.order.impl;

import java.util.ArrayList;
import java.util.List;

import com.zhongjian.dao.entity.shopown.HmShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.address.result.PreviewOrderAddressResultDTO;
import com.zhongjian.dto.order.reult.PreviewOrderResultDTO;
import com.zhongjian.dto.order.shopown.result.PreviewOrderShopownResultDTO;
import com.zhongjian.service.order.OrderService;

public class OrderServiceImpl extends HmBaseService<HmShopownBean, Integer>
        implements OrderService {

    @Override
    public ResultDTO<PreviewOrderResultDTO> previewOrder(Integer uid,
            Integer[] sids, Integer isAppointment, String type, String extra,
            String isSelfMention) {
        ResultDTO<PreviewOrderResultDTO> resultDTO = new ResultDTO<PreviewOrderResultDTO>();
        PreviewOrderResultDTO orderResultDTO = new PreviewOrderResultDTO();
        resultDTO.setFlag(false);
        // 根据isAppointment 判断是否为预约单。 是，配送时间为第二天8.48分。 否 配送时间为当前实际+48分
        if (isAppointment == 1) {
        	
			orderResultDTO.setDeliveryTime("");
		}
        orderResultDTO.setDeliveryTime("配送时间");

        // 获取默认收货地址 取到就存。没取到就不存
        PreviewOrderAddressResultDTO addressResultDTO = new PreviewOrderAddressResultDTO();
        orderResultDTO.setAddressResultDTO(addressResultDTO);

        // 获取所有待支付的商铺信息存进去。中间包含价格计算规则
        List<PreviewOrderShopownResultDTO> shopownResultDTOs = new ArrayList<PreviewOrderShopownResultDTO>();
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
        resultDTO.setData(orderResultDTO);
        return resultDTO;
    }

}
