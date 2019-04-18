package com.zhongjian.service.order.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.order.shopown.OrderShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.cart.storeActivity.result.CartStoreActivityResultDTO;
import com.zhongjian.dto.order.order.query.OrderStatusQueryDTO;
import com.zhongjian.service.order.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.zhongjian.util.DateUtil;
import com.zhongjian.util.RandomUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service("orderService")
public class OrderServiceImpl extends HmBaseService<OrderShopownBean, Integer> implements OrderService {


    @Autowired
    private OrderDao orderDao;

    @Override
    @Transactional
    public Map<String, Object> previewOrCreateOrder(Integer uid, Integer[] sids, String type, Integer extra,
                                                    String isSelfMention, boolean toCreateOrder, Integer addresId, Integer unixTime, Integer isAppointment) {
        Integer isVIp = 0;
        String vipFavour = "";
        String deliverfee = "￥6";
        BigDecimal deliverfeeBigDecimal = new BigDecimal("6");
        Integer integralCanUse = 0;
        Integer couponCanUse = 0;
        // 积分描述
        String integralContent = "";
        // 优惠券描述
        String couponContent = "";
        Integer marketId = 0;
        // 该单应付总价
        BigDecimal needPay = BigDecimal.ZERO;
        // 该单应付总价
        String needPayString = "";
        // 各商户总价和
        BigDecimal storesAmountBigDecimal = BigDecimal.ZERO;
        String storesAmountString = "";
        // 各商户参与优惠总价和
        BigDecimal storesAmountBigDecimalForFavorable = BigDecimal.ZERO;
        // 商户价格列表
        boolean showMarketActivity = false;
        String marketActivity = "";
        String marketActivityContent = "";
        String priceForIntegralCoupon = "";
        // 用于生成订单
        BigDecimal storeActivityPrice = BigDecimal.ZERO; // 商户优惠
        // 积分扣除
        BigDecimal integralSub = BigDecimal.ZERO;
        boolean integralPay = false;
        // 市场优惠
        BigDecimal marketActivityPrice = BigDecimal.ZERO;

        // vip优惠
        BigDecimal vipFavourRiderOrder = BigDecimal.ZERO;

        int createTime = (int) (System.currentTimeMillis() / 1000);
        // 小订单拼接
        StringBuilder orderJoint = new StringBuilder();
        Map<String, Object> storeOrders = null;// 总集合
        List<String> storeIds = null;
        if (toCreateOrder) {
            storeOrders = new HashMap<String, Object>();
            storeIds = new ArrayList<String>();
        }
        List<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < sids.length; i++) {
            Map<String, Object> storeOrderInfo = null;
            List<Map<String, Object>> hmCartList = null;

            if (toCreateOrder) {
                hmCartList = new ArrayList<Map<String, Object>>();
                storeOrderInfo = new HashMap<String, Object>();

            }
            Map<String, Object> store = new HashMap<String, Object>();
            // 查出价格商户名(原价)
            BigDecimal storeAmountBigDecimal = BigDecimal.ZERO;
            // 商户活动后价格
            BigDecimal actualStoreAmountBigDecimal = BigDecimal.ZERO;
            List<Map<String, Object>> singleStoreInfoList = orderDao.getBasketByUidAndSid(sids[i], uid);
            int unFavorable = 0;// 默认参与市场优惠
            String sname = "";
            String sid = "";
            if (singleStoreInfoList.size() == 0) {
                continue;
            }
            for (Iterator<Map<String, Object>> iterator = singleStoreInfoList.iterator(); iterator.hasNext(); ) {
                Map<String, Object> hmCart = null;// 用于生成订单hm_cart
                boolean flag = true;
                Map<String, Object> map = (Map<String, Object>) iterator.next();
                if (flag == true) {
                    marketId = (Integer) map.get("marketid");
                    sname = (String) map.get("sname");// 商户名
                    sid = map.get("pid").toString();
                    unFavorable = (int) map.get("unFavorable");
                    flag = false;
                }

                BigDecimal amoBigDecimal = (BigDecimal) map.get("amount");// 数量
                BigDecimal priceBigDecimal = map.get("price") == null ? (BigDecimal) map.get("basketprice")
                        : (BigDecimal) map.get("price");
                BigDecimal singleAmount = amoBigDecimal.multiply(priceBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);

                if (toCreateOrder) {
                    hmCart = new HashMap<String, Object>();
                    Integer gid = (Integer) map.get("gid");
                    String gname = (String) map.get("gname");
                    String unit = (String) map.get("unit");
                    String remark = (String) map.get("remark");
                    hmCart.put("gid", gid);
                    hmCart.put("gname", gname);
                    hmCart.put("unit", unit == null ? "个" : unit);
                    hmCart.put("uid", uid);
                    hmCart.put("price", singleAmount);
                    hmCart.put("amount", amoBigDecimal);
                    hmCart.put("sid", sid);
                    hmCart.put("status", 10005);
                    hmCart.put("ctime", createTime);
                    hmCart.put("remark", remark);
                    hmCart.put("oid", 0);
                    hmCartList.add(hmCart);
                }
                storeAmountBigDecimal = storeAmountBigDecimal.add(singleAmount);
            }
            if (unFavorable == 0) {
                // 享受市场优惠
                storesAmountBigDecimalForFavorable = storesAmountBigDecimalForFavorable.add(storeAmountBigDecimal);
            }
            CartStoreActivityResultDTO storeActivtiy = orderDao.getStoreActivtiy(sids[i], storeAmountBigDecimal);
            if (storeActivtiy == null) {
                actualStoreAmountBigDecimal = storeAmountBigDecimal;
            } else {
                if (storeActivtiy.getType() == 0) {
                    actualStoreAmountBigDecimal = storeAmountBigDecimal
                            .subtract(new BigDecimal(storeActivtiy.getReduce())).setScale(2, BigDecimal.ROUND_HALF_UP);
                } else {
                    actualStoreAmountBigDecimal = storeAmountBigDecimal
                            .multiply(new BigDecimal(storeActivtiy.getDiscount()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                }
            }
            store.put("sid", sid);
            store.put("sname", sname);
            store.put("totalPrice", storeAmountBigDecimal.setScale(2).toString());
            store.put("realPrice", actualStoreAmountBigDecimal.toString());
            // 计算商户优惠的价格
            if (toCreateOrder) {
                storeActivityPrice = storeActivityPrice
                        .add(storeAmountBigDecimal.subtract(actualStoreAmountBigDecimal));
                // 为生成hm_order做准备
                String smallOrderSn = "HM" + RandomUtil.getFlowNumber();
                if (orderJoint.length() == 0) {
                    orderJoint.append(smallOrderSn);
                } else {
                    orderJoint.append("|").append(smallOrderSn);
                }
                storeOrderInfo.put("pid", Integer.valueOf(sid));
                storeOrderInfo.put("order_sn", smallOrderSn);
                storeOrderInfo.put("uid", uid);
                storeOrderInfo.put("marketid", marketId);
                storeOrderInfo.put("total", storeAmountBigDecimal);
                storeOrderInfo.put("payment", actualStoreAmountBigDecimal);
                storeOrderInfo.put("ctime", createTime);
                storeOrderInfo.put("is_appointment", isAppointment);
                storeOrderInfo.put("roid", 0);
                storeOrderInfo.put("cartList", hmCartList);
                storeOrders.put(sid, storeOrderInfo);
                storeIds.add(sid);
            }
            storeList.add(store);
            storesAmountBigDecimal = storesAmountBigDecimal.add(actualStoreAmountBigDecimal);
        }
        storesAmountString = storesAmountBigDecimal.setScale(2).toString();
        needPay = storesAmountBigDecimal;
        // 检测市场活动
        Map<String, Object> marketActivtiy = orderDao.getMarketActivtiy(marketId);
        if (marketActivtiy != null) {
            showMarketActivity = true;
            String rule = (String) marketActivtiy.get("rule");
            int upLimit = (int) marketActivtiy.get("up_limit");
            int marketActivtiyType = (int) marketActivtiy.get("type");
            String[] split = null;
            if (marketActivtiyType == 0) {
                split = rule.split("-");
                marketActivity = "首单买满" + split[0] + "减" + split[1];
            } else {
                marketActivity = "首单买满" + upLimit + "打" + (int) (Float.valueOf(rule) * 10);
            }

            // 检查首单
            if (!orderDao.checkFirstOrderByUid(uid)) {
                marketActivityContent = "仅限当日首单";
            } else {
                // 没有选择优惠券或积分
                if ("0".equals(type)) {
                    BigDecimal upBigDecimal = new BigDecimal(upLimit);
                    if (storesAmountBigDecimalForFavorable.compareTo(upBigDecimal) >= 0) {
                        BigDecimal needSuBigDecimal = BigDecimal.ZERO;
                        if (split != null) {
                            needSuBigDecimal = new BigDecimal(split[1]);
                            needPay = needPay.subtract(needSuBigDecimal);
                        } else {
                            needSuBigDecimal = upBigDecimal.subtract(
                                    new BigDecimal(rule).multiply(upBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP));
                            needPay = needPay.subtract(needSuBigDecimal);
                        }
                        marketActivityPrice = needSuBigDecimal;
                        marketActivityContent = "-￥" + needSuBigDecimal.setScale(2).toString();
                    }
                    marketActivityContent = "未达到满减额度";
                } else {
                    marketActivityContent = "现金券不与菜场活动共享";
                }
            }

        }
        Map<String, Object> uMap = orderDao.getIntegralAndVipInfo(uid);
        Integer integral = (Integer) (uMap.get("integral"));
        integralContent = "[" + integral + "积分]可抵抗扣";
        if (integral > 0) {
            integralCanUse = 1;
        }
        Integer vipStatus = (Integer) (uMap.get("vip_status"));
        Integer vipexpire = (Integer) (uMap.get("vip_expire"));
        BigDecimal vipFavourable = needPay.multiply(new BigDecimal("0.05")).setScale(2, BigDecimal.ROUND_HALF_UP);
        vipFavourRiderOrder = vipFavourable;
        vipFavour = "￥" + vipFavourable.setScale(2).toString();
        // 判断是否是会员
        if (vipStatus == 1 && vipexpire > (System.currentTimeMillis() / 1000)) {
            isVIp = 1;
            needPay = needPay.subtract(vipFavourable);
            deliverfeeBigDecimal = new BigDecimal("3");
            deliverfee = "￥3";
            if ("1".equals(isSelfMention)) {
                deliverfee = "￥1";
                deliverfeeBigDecimal = new BigDecimal("1");
            }
        }

        if (orderDao.getCouponsNum(uid) > 0) {
            couponContent = "有优惠券可用";
            couponCanUse = 1;
        } else {
            couponContent = "暂无可用";
        }
        boolean needPayNeedHandleFlag = true;
//		算积分或优惠券
        BigDecimal priceForIntegralorCoupon = needPay.add(deliverfeeBigDecimal);
        priceForIntegralCoupon = priceForIntegralorCoupon.setScale(2).toString();
        if ("1".equals(type)) {
            BigDecimal hundredBigDecimal = new BigDecimal(100);
            // 使用积分
            BigDecimal integralBigDecimal = new BigDecimal(integral);
            BigDecimal integralPrice = integralBigDecimal.divide(hundredBigDecimal);
            if (priceForIntegralorCoupon.compareTo(integralPrice) > 0) {
                integralContent = "[余" + integral + "积分] -" + "￥" + integralPrice.setScale(2).toString();
                needPay = priceForIntegralorCoupon.subtract(integralPrice);
                if (toCreateOrder) {
                    integralSub = integralBigDecimal;
                }
                needPayNeedHandleFlag = false;
            } else {
                // 全积分支付
                integralContent = "[余" + integral + "积分] -" + "￥" + priceForIntegralorCoupon.setScale(2).toString();
                needPay = BigDecimal.ZERO;
                if (toCreateOrder) {
                    integralPay = true;
                    integralSub = priceForIntegralorCoupon.multiply(hundredBigDecimal);
                }
                needPayNeedHandleFlag = false;
            }
        }
        if ("2".equals(type)) {
            Map<String, Object> couponInfo = orderDao.getCouponInfo(uid, extra);
            if (couponInfo != null) {
                BigDecimal payFullBigDecimal = (BigDecimal) couponInfo.get("pay_full");
                Integer couponType = (Integer) couponInfo.get("type");
                if (couponType == 0) {
                    if (priceForIntegralorCoupon.compareTo(payFullBigDecimal) >= 0) {
                        BigDecimal couponPrice = (BigDecimal) couponInfo.get("coupon");
                        needPay = priceForIntegralorCoupon.subtract(couponPrice);
                        couponContent = "优惠金额-￥" + couponPrice.toString();
                        // 卡优惠券
                        if (toCreateOrder) {
                            storeOrders.put("couponid", extra);
                            storeOrders.put("coupon_price", couponPrice);
                        }
                        needPayNeedHandleFlag = false;
                    }
                } else {
                    // 配送券
                    deliverfeeBigDecimal = BigDecimal.ZERO;
                    deliverfee = "￥0";
                    needPayNeedHandleFlag = false;
                }
            }
        }
        // 最终价格补算
        if (needPayNeedHandleFlag) {
            needPay = needPay.add(deliverfeeBigDecimal);
        }
        needPayString = "￥" + needPay.toString();

        // 数据封装

        // 生成订单
        if (toCreateOrder) {
            storeOrders.put("rider_sn", "RI" + RandomUtil.getFlowNumber());
            storeOrders.put("order_sn", orderJoint.toString());
            storeOrders.put("uid", uid);
            storeOrders.put("marketid", marketId);
            storeOrders.put("rider_pay", deliverfeeBigDecimal);
            storeOrders.put("address_id", addresId);
            storeOrders.put("integral", integralSub == BigDecimal.ZERO ? null : integralSub);
            storeOrders.put("totalPrice", needPay);
            storeOrders.put("ctime", createTime);
            storeOrders.put("service_time", unixTime);
            storeOrders.put("is_appointment", isAppointment);
            storeOrders.put("original_price", storesAmountBigDecimal);
            storeOrders.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));//生成订单的时候三方号同时生成
            storeOrders.put("market_activity_price",
                    marketActivityPrice == BigDecimal.ZERO ? null : marketActivityPrice);
            storeOrders.put("store_activity_price", storeActivityPrice);
            storeOrders.put("vip_relief", vipFavourRiderOrder);
            storeOrders.put("pay_status", 0);
            storeOrders.put("rider_status", 0);
            if (integralPay) {
                //回调
                //1.支付状态变化
                //2.订单骑手生成
                //3.订单地址生成
                //4.积分增加(totalPrice数值)
                //5.订单扣除积分记录
                //6.发消息
                //7.默认市场
                //8.默认地址
                // 插入骑手
                Integer rid = getRidFormMarket(marketId, "random");
                if (rid == -1) {
                    throw new RuntimeException("系统调度中");
                }
                storeOrders.put("rid", rid);
                // 全积分支付
                storeOrders.put("pay_status", 1);
                storeOrders.put("pay_time", createTime);
            }
            if ("1".equals(isSelfMention)) {
                storeOrders.put("rider_status", 3);
            }
            Integer roid = orderDao.addHmRiderOrder(storeOrders);
            for (String storeId : storeIds) {
                Object sobj = storeOrders.get(storeId);
                if (sobj != null) {
                    Map<String, Object> sInfo = (Map<String, Object>) sobj;
                    sInfo.put("roid", roid);
                    sInfo.put("pay_status", storeOrders.get("pay_status"));
                    sInfo.put("order_status", storeOrders.get("rider_status"));
                    Integer oid = orderDao.addHmOrder(sInfo);
                    List<Map<String, Object>> cartList = (List<Map<String, Object>>) sInfo.get("cartList");
                    for (Map<String, Object> cart : cartList) {
                        cart.put("oid", oid);
                        orderDao.addHmCart(cart);
                    }
                }
            }
            return null;
        }
        return null;

    }

    Integer getRidFormMarket(Integer marketId, String strategy) {
        List<Integer> ridList = orderDao.getRidBymarketId(marketId);
        if (ridList.size() == 0) {
            return -1;
        }
        if ("random".equals(strategy)) {
            Random random = new Random();
            int n = random.nextInt(ridList.size());
            return ridList.get(n);
        }
        return -1;
        // 负载均衡
    }

    @Override
    // 付款时检测商户状态服务 ext增加检测是否同一市场
    public ResultDTO<Object> judgeHmShopownStatus(OrderStatusQueryDTO orderStatusQueryDTO) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<Boolean>();
        resultDTO.setFlag(false);
        if (null == orderStatusQueryDTO.getPids() || 0 == orderStatusQueryDTO.getPids().size()) {
            return ResultUtil.getFail(CommonMessageEnum.PID_IS_NULL);
        }
        if (null == orderStatusQueryDTO.getStatus()) {
            return ResultUtil.getFail(CommonMessageEnum.STATUS_IS_NULL);
        }
        List<OrderShopownBean> orderShopownBeans = this.dao.executeListMethod(orderStatusQueryDTO,
                "selectHmShopownStatusByPids", OrderShopownBean.class);
        // 默认返回状态匹配
        resultDTO.setFlag(true);
        resultDTO.setData(true);
        for (OrderShopownBean orderShopownBean : orderShopownBeans) {
            // 如果商户状态与传入状态不匹配，返回false
            if (!orderStatusQueryDTO.getStatus().equals(orderShopownBean.getStatus())) {
                resultDTO.setData(false);
                break;
            }
            // 如果店铺状态为预约中，但是该店铺没有开启预约 返回false
            if (2 == orderShopownBean.getStatus() && 0 == orderShopownBean.getIsAppointment()) {
                resultDTO.setData(false);
                break;
            }
            // 如果店铺状态为打烊或开张并且店铺为开启预约。返回false
            if ((1 == orderShopownBean.getStatus() || 0 == orderShopownBean.getStatus())
                    && 1 == orderShopownBean.getIsAppointment()) {
                resultDTO.setData(false);
                break;
            }
        }
        return ResultUtil.getSuccess(CommonMessageEnum.SUCCESS);
    }

    @Override
    public String previewOrderTime(Integer isAppointment) {
        // 传值如果传0则为没有预约就将当前时间加上48分返回. 如果传1则为预约就为当前时间加1天后的第二天的8点48精确到分.
        // 如果当前时间为这个月的最后一天,则显示为下个月的1号8点48,如果为今年的最后一天.则显示为明年的1月1号的8点48分.
        if (FinalDatas.ONE != isAppointment) {
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, 48);
            String format = DateUtil.HourMinute.format(nowTime.getTime());
            return format;
        } else {
            // 首先先判断今天是否为这个月的最后一天.如果不是最后一天则为第二天的08:48分
            // 获取今天的天数
            String today = DateUtil.day.format(new Date());
            // 获取这个月的月份
            String month = DateUtil.month.format(new Date());
            // 获取这个月最后一天的天数.
            Date lastDayOfMonth = DateUtil.getLastDayOfMonth(new Date());
            String lastDay = DateUtil.day.format(lastDayOfMonth);
            // 如果等于0那么.今天则为最后一天.那么就是月数加1后的08点48分.
            try {
                if (today.compareTo(lastDay) == 0) {
                    // 如果当前是12月.则直接将月份设置为1月.如果不是则月份加1;
                    Date date;
                    if (FinalDatas.twelve.compareTo(month) == 0) {
                        date = DateUtil.getFirstDayOfYear(new Date());
                    } else {
                        // 将月数加1
                        date = DateUtil.addMonthFroDate(new Date(), 1); // 2019/05/30 20:00
                    }
                    // 获取今天的最小时间
                    Date parse = DateUtil.lastDayTime.parse(DateUtil.formateDate(date) + " 00:00:00");
                    Calendar nowTime = Calendar.getInstance();
                    nowTime.setTime(parse);
                    // 天数设置为第一天
                    nowTime.set(Calendar.DATE, 1);
                    nowTime.add(Calendar.HOUR_OF_DAY, 8);
                    nowTime.add(Calendar.MINUTE, 48);
                    String format = DateUtil.lastDayTime.format(nowTime.getTime());
                    return format;
                } else {
                    Date parse = DateUtil.lastDayTime.parse(DateUtil.formateDate(new Date()) + " 00:00:00");
                    Date date = DateUtil.addDayFroDate(parse, 1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR_OF_DAY, 8);
                    calendar.add(Calendar.MINUTE, 48);
                    // 获取第二天的08点48分.
                    String format = DateUtil.lastDayTime.format(calendar.getTime());
                    return format;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Map<String, Object> getOutTradeNoAndAmount(Integer uid, Integer orderId, String business) {
        if (business.equals("RIO")) {
            Map<String, Object> orderInfo = orderDao.getDetailByOrderId(uid, orderId);
            return orderInfo;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", "").length());
    }
}
