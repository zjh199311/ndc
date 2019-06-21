package com.zhongjian.dto.order.order.result;

import com.zhongjian.dto.cart.address.result.OrderAddressResultDTO;
import com.zhongjian.dto.cart.basket.result.CartBasketResultDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class OrderItemResultDTO implements Serializable {

    private static final long serialVersionUID = -2548785476444048539L;

    /**
     * id
     */
    private Integer id;

    /**
     * 菜场id
     */
    private Integer marketid;

    /**
     * 骑手id
     */
    private Integer rid;

    /**
     * 订单编号
     */
    private String riderSn;

    /**
     * addressId
     */
    private Integer addressId;

    /**
     * 1 会员 0非会员
     */
    private Integer isMember;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 骑手状态
     */
    private Integer riderStatus;

    /**
     * 骑手名字
     */
    private String riderUserName;

    /**
     * 骑手手机号码
     */
    private String riderPhone;

    /**
     * 0配送 3自提
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusMsg;

    /**
     * 商品订单原价，当实际价格与原价相同，显示为空
     */
    private String orderTotal;

    /**
     * 商品订单实际价格
     */
    private String orderPayment;

    /**
     * 会员折扣优惠价格
     */
    private String delMemberPrice;

    /**
     * 商品总价
     */
    private String totalPrice;

    /**
     * 会员折扣描述
     */
    private String memberContent;


    /**
     * 会员自提描述
     */
    private String selfLifting;

    /**
     * 预约时间时间戳
     */
    private Integer time;

    /**
     * 下单时间戳
     */
    private Integer ptime;

    /**
     * 下单时间
     */
    private String payTime;

    /**
     * 送达时间时间戳
     */
    private Integer finishTime;

    /**
     * 送达时间
     */
    private String serviceTime;

    /**
     * 创建时间戳
     */
    private Integer ctime;

    /**
     * 创建时间
     */
    private String createTime;


    /**
     * 预约时间
     */
    private String serverTime;


    /**
     * 积分优惠
     */
    private String integralPrice;

    /**
     * 菜场优惠
     */
    private String marketActivityPrice;

    /**
     * 优惠券优惠
     */
    private String couponPrice;

    /**
     * 配送费
     */
    private String distributionFee;

    /**
     * 支付总费用
     */
    private String foodPrice;

    /**
     * 活动优惠
     */
    private String activityPrice;


    /**
     * 地址
     */
    private OrderAddressResultDTO address;

    /**
     * 订单
     */
    private List<OrderListResultDTO> storeList;



}
