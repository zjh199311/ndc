package com.zhongjian.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.common.IdWorkers;
import com.zhongjian.dao.entity.order.shopown.OrderShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.jdbctemplate.CVOrderDao;
import com.zhongjian.dao.jdbctemplate.IntegralVipDao;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dto.cart.storeActivity.result.CartStoreActivityResultDTO;
import com.zhongjian.exception.NDCException;
import com.zhongjian.service.order.CVOrderService;
import com.zhongjian.service.order.OrderService;
import com.zhongjian.shedule.OrderShedule;
import com.zhongjian.util.DateUtil;
import com.zhongjian.util.RandomUtil;

@Service("cvOrderService")
public class CVOrderServiceImpl extends HmBaseService<OrderShopownBean, Integer> implements CVOrderService {

	@Autowired
	private CVOrderDao cvOrderDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private IntegralVipDao integralVipDao;

	@Autowired
	private IdWorkers idWorkers;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderShedule orderShedule;

	@Override
	public Map<String, Object> previewCVOrder(Integer uid, Integer sid, String type, Integer extra,String isSelfMention) {
		// 结果集定义
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> goodMaps = new ArrayList<Map<String, Object>>();
		// 商铺下商品总价
		BigDecimal storeAmountBigDecimal = BigDecimal.ZERO;
		// 商户活动后价格
		BigDecimal actualStoreAmountBigDecimal = BigDecimal.ZERO;
		// 商户名
		String sname = "";
		// 会员折扣
		Double memberDiscount = 1d;
		// 会员单笔limit
		Double limitOne = 200d;
		Double limitDayRelief = 10d;
		// 积分描述
		String integralContent = "";
		Integer integralCanUse = 0;
		String couponContent = "";
		Integer couponCanUse = 0;
		// 是否是vip
		Integer isVIp = 0;
		// 该单应付总价
		BigDecimal needPay = BigDecimal.ZERO;
		// 优惠券判断
		String priceForCouponString = "";
		String integralPriceString = "";
		// 总价
		String needPayString = "";
		String vipFavour = "";
		
		String deliverFee = "2";
		if ("1".equals(isSelfMention)) {
			deliverFee = "0";
		}
		List<Map<String, Object>> storeGoodsList = cvOrderDao.getCVStoreByUidAndSid(sid, uid);
		// 查出购物车详情
		for (Iterator<Map<String, Object>> iterator = storeGoodsList.iterator(); iterator.hasNext();) {
			Map<String, Object> cvStore = (Map<String, Object>) iterator.next();
			boolean flag = true;
			if (flag == true) {
				sname = (String) cvStore.get("sname");// 商户名
				flag = false;
			}
			BigDecimal amoBigDecimal = (BigDecimal) cvStore.get("amount");// 数量
			BigDecimal priceBigDecimal = cvStore.get("price") == null ? (BigDecimal) cvStore.get("basketprice")
					: (BigDecimal) cvStore.get("price");
			BigDecimal singleAmount = amoBigDecimal.multiply(priceBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
			Map<String, Object> good = new HashMap<String, Object>();
			String gname =  cvStore.get("gname") == null? "其他" : (String) cvStore.get("gname");
			good.put("gname", gname);
			good.put("price", singleAmount.toString());
			goodMaps.add(good);
			storeAmountBigDecimal = storeAmountBigDecimal.add(singleAmount);
		}
		CartStoreActivityResultDTO storeActivtiy = orderDao.getStoreActivtiy(sid, storeAmountBigDecimal);
		if (storeActivtiy == null) {
			actualStoreAmountBigDecimal = storeAmountBigDecimal;
		} else {
			if (storeActivtiy.getType() == 0) {
				actualStoreAmountBigDecimal = storeAmountBigDecimal.subtract(new BigDecimal(storeActivtiy.getReduce()))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				actualStoreAmountBigDecimal = storeAmountBigDecimal
						.multiply(new BigDecimal(storeActivtiy.getDiscount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		needPay = actualStoreAmountBigDecimal;
		// 积分优惠券初始化显示----start
		Map<String, Object> uMap = integralVipDao.getIntegralAndVipInfo(uid);
		Integer integral = (Integer) (uMap.get("integral"));
		integralContent = "共" + integral + "积分";
		if (integral > 0) {
			integralCanUse = 1;
		}
		boolean todayCouponUse = cvOrderDao.todayCouponUse(uid);
		if (cvOrderDao.getCouponsNumCanUse(uid) > 0) {
			if (todayCouponUse) {
				couponContent = "有可用优惠券";
				couponCanUse = 1;
			} else {
				couponContent = "每日一张限用一张";
			}
		} else {
			couponContent = "";
		}
		Integer vipStatus = (Integer) (uMap.get("vip_status"));
		// 动态获取VIP参数 --start
		Map<String, Object> config = getConfigByUidAndStatus(uid, vipStatus);
		limitDayRelief = config.get("cvlimitDayRelief") == null ? limitDayRelief
				: (Double) config.get("cvlimitDayRelief");
		limitOne = config.get("cvlimitOne") == null ? limitOne : (Double) config.get("cvlimitOne");
		memberDiscount = config.get("cvdiscount") == null ? memberDiscount : (Double) config.get("cvdiscount");

		// 动态获取VIP参数 --end
		BigDecimal subtract = new BigDecimal(1).subtract(new BigDecimal(memberDiscount));
		Double canFavourOne = subtract.multiply(new BigDecimal(limitOne)).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		Double vipRelief = cvOrderDao.getVipRelief(uid);
		Double canFavourDay = limitDayRelief - vipRelief;
		if (canFavourDay < 0.00) {
			canFavourDay = 0.00;
		}
		BigDecimal vipFavourMoney = BigDecimal.ZERO;
		if (canFavourDay < canFavourOne) {
			vipFavourMoney = new BigDecimal(canFavourDay);
		} else {
			vipFavourMoney = new BigDecimal(canFavourOne);
		}
		BigDecimal vipFavourable = subtract.multiply(needPay).setScale(2, BigDecimal.ROUND_HALF_UP);
		if (vipFavourable.compareTo(vipFavourMoney) == 1) {
			vipFavourable = vipFavourMoney;
		}
		// 判断是否是会员
		if (vipStatus == 1) {
			isVIp = 1;
			needPay = needPay.subtract(vipFavourable);
		}
		BigDecimal deliverFeeB = new BigDecimal(deliverFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal priceForIntegral = needPay.add(deliverFeeB);
		BigDecimal priceForCoupon = needPay;
		priceForCouponString = priceForCoupon.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		if ("1".equals(type) && integral > 0) {
			BigDecimal hundredBigDecimal = new BigDecimal(100);
			// 使用积分
			BigDecimal integralBigDecimal = new BigDecimal(integral);
			BigDecimal integralPrice = integralBigDecimal.divide(hundredBigDecimal);

			if (priceForIntegral.compareTo(integralPrice) > 0) {
				integralContent = "共" + integral + "积分,可抵扣" + integral + "积分";
				integralPriceString = "-￥" + integralPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
				needPay = priceForIntegral.subtract(integralPrice);
			} else {
				// 全积分支付
				integralContent = "共" + integral + "积分，可抵扣"
						+ priceForIntegral.multiply(hundredBigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP) + "积分";
				integralPriceString = "-￥" + priceForIntegral.setScale(2, BigDecimal.ROUND_HALF_UP);
				needPay = BigDecimal.ZERO;
			}
		}

		else if ("2".equals(type) && todayCouponUse) {
			Map<String, Object> couponInfo = orderDao.getCouponInfo(uid, extra);
			if (couponInfo != null) {
				BigDecimal payFullBigDecimal = (BigDecimal) couponInfo.get("pay_full");
				Integer couponType = (Integer) couponInfo.get("type");
				if (couponType == 0) {
					if (priceForCoupon.compareTo(payFullBigDecimal) >= 0) {
						BigDecimal couponPrice = (BigDecimal) couponInfo.get("price");
						needPay = needPay.subtract(couponPrice);
						if (priceForCoupon.compareTo(couponPrice) < 0) {
							couponPrice = priceForCoupon;
							needPay = BigDecimal.ZERO;
						}
						needPay = needPay.add(deliverFeeB);
						couponContent = "-￥" + couponPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					}
				}
			}
		}else {
			needPay = needPay.add(deliverFeeB);
		}
		needPayString = needPay.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		// 数据封装
		resMap.put("sname", sname);
		resMap.put("isMember", isVIp);
		resMap.put("originalTotalPrice", "￥" + storeAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
		resMap.put("totalPrice", "￥" + actualStoreAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
		resMap.put("payTotal", needPayString);
		resMap.put("integralCanUse", integralCanUse);
		resMap.put("couponCanUse", couponCanUse);
		resMap.put("integralContent", integralContent);
		resMap.put("couponContent", couponContent);
		resMap.put("goodMap", goodMaps);
		resMap.put("integralPrice", integralPriceString);
		resMap.put("priceForCoupon", priceForCouponString);
		
		resMap.put("fee","￥" + deliverFeeB);
		//vip注释 --start
//		vipFavour = vipFavourable.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//		if (isVIp == 0) {
//			resMap.put("memberContent", "会员用户专享");
//			resMap.put("delMemberPrice", "开通会员，预计最高可为您节省" + vipFavour + "元");
//		} else {
//			if (vipFavourable.compareTo(BigDecimal.ZERO) == 0) {
//				resMap.put("memberContent", "当日vip优惠额度达到上限");
//				resMap.put("delMemberPrice", "");
//			} else {
//				resMap.put("delMemberPrice", "-￥" + vipFavour);
//				String disCountStr = new BigDecimal(memberDiscount).multiply(new BigDecimal(100))
//						.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//				resMap.put("memberContent", "会员享" + getHan(disCountStr));
//			}
//
//		}
		//vip注释 --end
		return resMap;
	}

	@Override
	public String previewOrderTime() {
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, 20);
		String format = DateUtil.lastDayTime.format(nowTime.getTime());
		return format;
	}

	@Override
	@Transactional
	public boolean cancelOrder(Integer orderId) {
		if (cvOrderDao.updateUCVStatusToTimeout(orderId)) {
			cvOrderDao.updateCVOrderToTimeout(orderId);
			Map<String, Object> orderDetail = cvOrderDao.getOrderDetailById(orderId);
			BigDecimal vipReliefReturn = (BigDecimal) orderDetail.get("vip_relief");
			BigDecimal integralPrice = (BigDecimal) orderDetail.get("integralPrice");
			Object couponId = orderDetail.get("coupon_id");
			Integer uid = (Integer) orderDetail.get("uid");
			if (integralPrice.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal integralReturn = integralPrice.multiply(new BigDecimal("100"));
				integralVipDao.updateUserIntegral(uid, "+", integralReturn.intValue());
			}
			Map<String, Object> cvUserOrderRecord = cvOrderDao.getCVUserOrderRecord(uid);
			BigDecimal currentVipRelief = (BigDecimal) cvUserOrderRecord.get("vip_relief");
			//退回vip减免
			BigDecimal remainVipRelief = currentVipRelief.subtract(vipReliefReturn).setScale(2, BigDecimal.ROUND_HALF_UP);
			Integer orderNum = (Integer) cvUserOrderRecord.get("order_num") - 1;
			Integer todayOrderNum = (Integer) cvUserOrderRecord.get("today_order_num") - 1;
			cvUserOrderRecord.put("uid", uid);
			cvUserOrderRecord.put("vip_relief", remainVipRelief);
			cvUserOrderRecord.put("order_num", orderNum);
			cvUserOrderRecord.put("today_order_num", todayOrderNum);
			cvOrderDao.recordUpdate(cvUserOrderRecord);
			if (couponId != null) {
				cvOrderDao.recordCouponCancel(uid);
				orderDao.changeCouponToZero((Integer) couponId);
			}
			return true;
		}
		return false;
	}

	private Map<String, Object> getConfigByUidAndStatus(Integer uid, Integer vipStatus) {
		if (vipStatus == 0) {
			return integralVipDao.getDefualtVipConfig();
		} else {
			// 如果是会员
			return integralVipDao.getVipConfigByUid(uid);
		}
	}

	public static String getHan(String numberString) {
		StringBuilder result = new StringBuilder();
		if (numberString.length() == 2 && numberString.contains("0")) {
			numberString = numberString.substring(0, 1);
		} else if (numberString.length() == 1) {
			result.append("零点");
		}
		for (int i = 0; i < numberString.length(); i++) {
			char current = numberString.charAt(i);
			if (current == '9') {
				result.append("九");
			} else if (current == '8') {
				result.append("八");
			} else if (current == '7') {
				result.append("七");
			} else if (current == '6') {
				result.append("六");
			} else if (current == '5') {
				result.append("五");
			} else if (current == '4') {
				result.append("四");
			} else if (current == '3') {
				result.append("三");
			} else if (current == '2') {
				result.append("二");
			} else if (current == '1') {
				result.append("一");
			}
		}
		result.append("折");
		return result.toString();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> createOrder(Integer uid, Integer sid, String type, Integer extra, String isSelfMention,
			Integer addressId, Integer unixTime) throws NDCException {
		// 结果集定义
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> goodMaps = new ArrayList<Map<String, Object>>();
		// store order
		Map<String, Object> cvOrderMap = new HashMap<String, Object>();
		// user order
		Map<String, Object> userOrderMap = new HashMap<String, Object>();
		// 商铺下商品总价
		BigDecimal storeAmountBigDecimal = BigDecimal.ZERO;
		// 商户活动后价格
		BigDecimal actualStoreAmountBigDecimal = BigDecimal.ZERO;
		// 商户名
		Integer dataSid = 0;
		// 会员折扣
		Double memberDiscount = 1d;
		// 会员单笔limit
		Double limitOne = 200d;
		Double limitDayRelief = 10d;
		// 积分使用
		BigDecimal integralPriceData = BigDecimal.ZERO;
		// 优惠券使用
		BigDecimal couponPriceData = BigDecimal.ZERO;
		// 该单应付总价
		BigDecimal needPay = BigDecimal.ZERO;
		
		String deliverFee = "2";
		if ("1".equals(isSelfMention)) {
			deliverFee = "0";
		}
		// 总价
		StringBuilder remarks = new StringBuilder();
		int createTime = (int) (System.currentTimeMillis() / 1000);
		List<Map<String, Object>> storeGoodsList = cvOrderDao.getCVStoreByUidAndSid(sid, uid);
		// 查出购物车详情
		for (Iterator<Map<String, Object>> iterator = storeGoodsList.iterator(); iterator.hasNext();) {
			Map<String, Object> cvStore = (Map<String, Object>) iterator.next();
			boolean flag = true;
			if (flag == true) {
				dataSid = (Integer) cvStore.get("pid");// 商户名
				flag = false;
			}
			BigDecimal amoBigDecimal = (BigDecimal) cvStore.get("amount");// 数量
			BigDecimal priceBigDecimal = cvStore.get("price") == null ? (BigDecimal) cvStore.get("basketprice")
					: (BigDecimal) cvStore.get("price");
			BigDecimal singleAmount = amoBigDecimal.multiply(priceBigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
			Map<String, Object> good = new HashMap<String, Object>();
			String gname = cvStore.get("gname") == null ? "其他" : (String) cvStore.get("gname");
			String unit = cvStore.get("unit") == null ? "个" : (String) cvStore.get("unit");
			good.put("gname", gname);
			good.put("price", singleAmount);
			good.put("amount", amoBigDecimal);
			good.put("unit", unit);
			good.put("gid", cvStore.get("gid"));
			good.put("uid", uid);
			good.put("sid", dataSid);
			good.put("ctime", createTime);
			String remark = (String) cvStore.get("remark");
			good.put("remark", remark);
			if (!"".equals(remark)) {
				remarks.append("[").append(gname).append("]").append(cvStore.get("remark")).append("\r\n");
			}
			goodMaps.add(good);
			storeAmountBigDecimal = storeAmountBigDecimal.add(singleAmount);
		}
		CartStoreActivityResultDTO storeActivtiy = orderDao.getStoreActivtiy(sid, storeAmountBigDecimal);
		if (storeActivtiy == null) {
			actualStoreAmountBigDecimal = storeAmountBigDecimal;
		} else {
			if (storeActivtiy.getType() == 0) {
				actualStoreAmountBigDecimal = storeAmountBigDecimal.subtract(new BigDecimal(storeActivtiy.getReduce()))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				actualStoreAmountBigDecimal = storeAmountBigDecimal
						.multiply(new BigDecimal(storeActivtiy.getDiscount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		needPay = actualStoreAmountBigDecimal;
		// 积分优惠券初始化显示----start
		Map<String, Object> uMap = integralVipDao.getIntegralAndVipInfo(uid);
		Integer integral = (Integer) (uMap.get("integral"));
		boolean todayCouponUse = cvOrderDao.todayCouponUse(uid);
		Integer vipStatus = (Integer) (uMap.get("vip_status"));

		BigDecimal vipFavourable = BigDecimal.ZERO;
		// 判断是否是会员
		if (vipStatus == 1) {
			// 动态获取VIP参数 --start
			Map<String, Object> config = getConfigByUidAndStatus(uid, vipStatus);
			limitDayRelief = config.get("cvlimitDayRelief") == null ? limitDayRelief
					: (Double) config.get("cvlimitDayRelief");
			limitOne = config.get("cvlimitOne") == null ? limitOne : (Double) config.get("cvlimitOne");
			memberDiscount = config.get("cvdiscount") == null ? memberDiscount : (Double) config.get("cvdiscount");

			// 动态获取VIP参数 --end
			BigDecimal subtract = new BigDecimal(1).subtract(new BigDecimal(memberDiscount));
			Double canFavourOne = subtract.multiply(new BigDecimal(limitOne)).setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			Double vipRelief = cvOrderDao.getVipRelief(uid);
			Double canFavourDay = limitDayRelief - vipRelief;
			if (canFavourDay < 0.00) {
				canFavourDay = 0.00;
			}
			BigDecimal vipFavourMoney = BigDecimal.ZERO;
			if (canFavourDay < canFavourOne) {
				vipFavourMoney = new BigDecimal(canFavourDay);
			} else {
				vipFavourMoney = new BigDecimal(canFavourOne);
			}
			vipFavourable = subtract.multiply(needPay).setScale(2, BigDecimal.ROUND_HALF_UP);
			if (vipFavourable.compareTo(vipFavourMoney) == 1) {
				vipFavourable = vipFavourMoney;
			}
			needPay = needPay.subtract(vipFavourable);
		}
		BigDecimal deliverFeeB = new BigDecimal(deliverFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal priceForIntegral = needPay.add(deliverFeeB);
		BigDecimal priceForCoupon = needPay;
		boolean useCoupon = false;
		if ("1".equals(type) && integral > 0) {
			BigDecimal hundredBigDecimal = new BigDecimal(100);
			// 使用积分
			BigDecimal integralBigDecimal = new BigDecimal(integral);
			BigDecimal integralPrice = integralBigDecimal.divide(hundredBigDecimal);
			if (priceForIntegral.compareTo(integralPrice) > 0) {
				integralPriceData = integralPrice;
				needPay = priceForIntegral.subtract(integralPrice);
			} else {
				// 全积分支付
				integralPriceData = priceForIntegral;
				needPay = BigDecimal.ZERO;
			}
			// 卡积分
			try {
				integralVipDao.updateUserIntegral(uid, "-", integralPriceData.multiply(hundredBigDecimal).intValue());
			} catch (RuntimeException e) {
				throw new NDCException.IntegralException();

			}
		}
		
		else if ("2".equals(type) && todayCouponUse) {
			Map<String, Object> couponInfo = orderDao.getCouponInfo(uid, extra);
			if (couponInfo != null) {
				BigDecimal payFullBigDecimal = (BigDecimal) couponInfo.get("pay_full");
				Integer couponType = (Integer) couponInfo.get("type");
				if (couponType == 0) {
					if (priceForCoupon.compareTo(payFullBigDecimal) >= 0) {
						useCoupon = true;
						BigDecimal couponPrice = (BigDecimal) couponInfo.get("price");
						needPay = needPay.subtract(couponPrice);
						if (priceForCoupon.compareTo(couponPrice) < 0) {
							couponPrice = priceForCoupon;
							needPay = BigDecimal.ZERO;
						}
						needPay = needPay.add(deliverFeeB);
						couponPriceData = couponPrice;
					}
				}
			}
		}else {
			needPay = needPay.add(deliverFeeB);
		}
		// 删除便利店购物车
		
		// 便利店商户订单封装
		cvOrderMap.put("order_sn", "CV" + idWorkers.getCVOrderIdWork().nextId());
		cvOrderMap.put("total", storeAmountBigDecimal);
		cvOrderMap.put("payment", actualStoreAmountBigDecimal);
		cvOrderMap.put("sid", sid);
		cvOrderMap.put("ctime", createTime);
		cvOrderMap.put("order_status", 0);
	
		cvOrderMap.put("addressid", addressId);
		cvOrderMap.put("pay_status", 0);
		cvOrderMap.put("deliver_fee", new BigDecimal(deliverFee).setScale(2, BigDecimal.ROUND_HALF_UP));
		cvOrderMap.put("remark", remarks.toString());
		cvOrderMap.put("service_fee", BigDecimal.ZERO);
		cvOrderMap.put("deliver_model", 0);
		cvOrderMap.put("service_time", unixTime);
		if ("1".equals(isSelfMention)) {
			cvOrderMap.put("ordertaking_time", createTime);
			cvOrderMap.put("order_status", 3);
			cvOrderMap.put("deliver_model", 1);
		}
		cvOrderMap.put("rid", 0);
		// 便利店用户订单封装
		String out_trade_no = idWorkers.getCVOutTradeIdWork().nextId() + "";
		userOrderMap.put("order_sn", "UCV" + idWorkers.getCVUserOrderIdWork().nextId());
		userOrderMap.put("out_trade_no", out_trade_no);
		userOrderMap.put("pay_status", 0);
		userOrderMap.put("uid", uid);
		userOrderMap.put("integralPrice", integralPriceData);
		userOrderMap.put("store_activity_price", storeAmountBigDecimal.subtract(actualStoreAmountBigDecimal));
		userOrderMap.put("totalPrice", needPay);
		userOrderMap.put("originalPrice", actualStoreAmountBigDecimal);
		userOrderMap.put("ctime", createTime);
		userOrderMap.put("vip_relief", vipFavourable);
		userOrderMap.put("deliver_fee", new BigDecimal(deliverFee).setScale(2, BigDecimal.ROUND_HALF_UP));
		// 添加记录表hm_cvuserorder_record
		if (useCoupon) {
			userOrderMap.put("coupon_price", couponPriceData);
			userOrderMap.put("coupon_id", extra);
			if (!orderDao.changeCouponToOne(extra)) {
				throw new NDCException.CouponException();
			}
		}
		Map<String, Object> cvUserOrderRecord = cvOrderDao.getCVUserOrderRecord(uid);
		if (cvUserOrderRecord == null) {
			Integer useCouponInt = useCoupon ? 1 : 0;
			cvUserOrderRecord = new HashMap<String, Object>();
			cvUserOrderRecord.put("vip_relief", vipFavourable.setScale(2, BigDecimal.ROUND_HALF_UP));
			cvUserOrderRecord.put("uid", uid);
			cvUserOrderRecord.put("order_num", 1);
			cvUserOrderRecord.put("today_order_num", 1);
			cvUserOrderRecord.put("coupon_use", useCouponInt);
			cvOrderDao.addCVUserOrderRecord(cvUserOrderRecord);
		} else {
			BigDecimal currentVipRelief = (BigDecimal) cvUserOrderRecord.get("vip_relief");
			cvUserOrderRecord.put("vip_relief",
					currentVipRelief.add(vipFavourable).setScale(2, BigDecimal.ROUND_HALF_UP));
			cvUserOrderRecord.put("order_num", (Integer) cvUserOrderRecord.get("order_num") + 1);
			cvUserOrderRecord.put("today_order_num", (Integer) cvUserOrderRecord.get("today_order_num") + 1);
			cvUserOrderRecord.put("uid", uid);
			cvOrderDao.recordUpdate(cvUserOrderRecord);
			if (useCoupon) {
				if (!cvOrderDao.recordCouponUse(uid)) {
					throw new NDCException.CouponException();
				}

			}
		}

		Integer cvUserOrderId = cvOrderDao.addCVUserOrder(userOrderMap);
		cvOrderMap.put("uoid", cvUserOrderId);
		Integer cVOrderId = cvOrderDao.addCVOrder(cvOrderMap);
		cvOrderDao.addCVOrderDetail(goodMaps, cVOrderId);
		if (needPay.compareTo(BigDecimal.ZERO) == 0) {
			orderService.handleROrder("CV" + RandomUtil.getRandom620(6) + out_trade_no, needPay.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),"integral");
			resMap.put("cvoid", cvUserOrderId);
			resMap.put("status", 1);
		} else {
			resMap.put("cvoid", cvUserOrderId);
			resMap.put("status", 0);
		}
		//订单检测
		orderShedule.checkCVOrder(cvUserOrderId);
		return resMap;
	}

	@Override
	public boolean judgeHmShopownStatus(Integer sid) {
		OrderShopownBean orderShopownBean = this.dao.executeSelectOneMethod(sid, "selectHmShopownStatusByPid",
				OrderShopownBean.class);
		if (orderShopownBean.getStatus() == 1) {
			return false;
		}
		return true;
	}
	@Override
	public void platformHandle(Integer cvOrderId) {
		Integer uoid = cvOrderDao.getUidByCVOrderId(cvOrderId);
		if (uoid != 0) {
			orderShedule.delayHandleCVOrder(uoid,0);
		}
	}
}
